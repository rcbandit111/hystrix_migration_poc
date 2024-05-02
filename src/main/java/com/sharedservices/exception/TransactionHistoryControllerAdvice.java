package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class TransactionHistoryControllerAdvice {

  /**
   * Handles HystrixBadRequestException.
   */
  @ExceptionHandler(HystrixBadRequestException.class)
  public final ResponseEntity<Object> handleHystrixBadRequestException(HystrixBadRequestException ex) {
    if (Objects.nonNull(ex)) {
      if (Objects.nonNull(ex.getCause()) && ex.getCause() instanceof ProcessingFailedException) {
        return null; // return custom exception
      } else {
        return null; // return custom exception
      }
    }
    return null;
  }

}
