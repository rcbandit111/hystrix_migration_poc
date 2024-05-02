package com.sharedservices.exception;

import com.netflix.hystrix.strategy.HystrixPlugins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings("PMD.UseUtilityClass")
public class Application {

  public static void main(String... args) {
    HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixCustomExecutionHook());
    SpringApplication.run(Application.class, args);
  }
}
