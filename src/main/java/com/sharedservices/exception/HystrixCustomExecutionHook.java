package com.sharedservices.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

public class HystrixCustomExecutionHook extends HystrixCommandExecutionHook {

  private static final String REQUEST_ATTRIBUTES = "REQUEST_ATTRIBUTES";
  private static final String MDC_CONTEXT_MAP = "MDC_CONTEXT_MAP";
  private final HystrixRequestVariableDefault<Map<String, Object>> contextVariables 
      = new HystrixRequestVariableDefault<>();

  @Override
  public <T> void onStart(HystrixInvokable<T> commandInstance) {
    storeContext();
  }

  @Override
  public <T> void onThreadStart(HystrixInvokable<T> commandInstance) {
    copyContext();
  }

  @Override
  public <T> void onThreadComplete(HystrixInvokable<T> commandInstance) {
    cleanup();
  }

  private void cleanup() {
    RequestContextHolder.resetRequestAttributes();
    MDC.clear();
  }

  private void storeContext() {
    Map<String, Object> variables = new HashMap<>();
    variables.put(MDC_CONTEXT_MAP, MDC.getCopyOfContextMap());
    variables.put(REQUEST_ATTRIBUTES, RequestContextHolder.getRequestAttributes());
    contextVariables.set(variables);
  }

  private void copyContext() {
    Map<String, Object> variables = contextVariables.get();
    if (variables.get(MDC_CONTEXT_MAP) != null) {
      MDC.setContextMap((Map<String, String>) variables.get(MDC_CONTEXT_MAP));
    }
    RequestContextHolder.setRequestAttributes((RequestAttributes) variables.get(REQUEST_ATTRIBUTES));
  }

}
