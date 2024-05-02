package com.sharedservices.exception;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.Callable;

import jakarta.annotation.PostConstruct;

@Configuration
public class HystrixConfiguration {

  /**
   * Resetting and updating hystrix config to wrap callable, so that the ThreadLocal MDC is propagated to the Hystrix
   * thread pool thread and cleared immediately after.
   */
  @PostConstruct
  public void init() {
    HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
    HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
    HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
    HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();

    HystrixPlugins.reset();
    configureHisrix(eventNotifier, propertiesStrategy, commandExecutionHook, metricsPublisher);
  }

  private void configureHisrix(HystrixEventNotifier eventNotifier, HystrixPropertiesStrategy propertiesStrategy,
      HystrixCommandExecutionHook commandExecutionHook, HystrixMetricsPublisher metricsPublisher) {
    HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
    HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
    HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
    HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
    HystrixPlugins.getInstance().registerConcurrencyStrategy(new HystrixConcurrencyStrategy() {

      @Override
      public <T> Callable<T> wrapCallable(Callable<T> callable) {
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
          try {
            MDC.setContextMap(contextMap);
            return callable.call();
          } finally {
            MDC.clear();
          }
        };
      }
    });
  }
}
