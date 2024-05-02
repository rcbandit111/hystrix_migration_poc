package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Common exception handler to handle REST errors consistently.
 */
@ControllerAdvice
public class ThreeDControllerAdvice extends ResponseEntityExceptionHandler {

  /**
   * Handles hystrix runtime exceptions with consistent responses and details.
   *
   * @param ex an unexpected exception
   * @return a standard ErrorResult
   */
  @ExceptionHandler(HystrixRuntimeException.class)
  public final ResponseEntity<Object> handleHystrixRuntimeException(HystrixRuntimeException ex) {
    System.out.println(String.format("Caught hystrix runtime exception: %s", ex.getMessage()));
    return null;
  }

}
