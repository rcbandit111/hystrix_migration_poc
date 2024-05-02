package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerAdvice {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

  @ExceptionHandler(HystrixRuntimeException.class)
  public BadRequestException handleHystrixException(HystrixRuntimeException hystrixException) {

    if (HystrixRuntimeException.FailureType.TIMEOUT == hystrixException.getFailureType()) {
      LOGGER.error("Caught hystrix timeout exception: ", hystrixException);
      return new BadRequestException();
    }

    System.out.format("Caught hystrix runtime exception: {}", hystrixException.getMessage());
    return null;
  }
}
