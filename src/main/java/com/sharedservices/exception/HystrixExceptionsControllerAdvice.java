package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler for hystrix specific errors.
 */
@ConditionalOnClass(HystrixRuntimeException.class)
@ControllerAdvice
@Order(CommonControllerAdvice.ORDER - 2)
public class HystrixExceptionsControllerAdvice {

  private static final Logger logger = LoggerFactory.getLogger(HystrixExceptionsControllerAdvice.class);

  /**
   * Handles hystrix runtime exceptions with consistent responses and details.
   *
   * @param ex an unexpected exception
   * @return a standard ErrorResult
   */
  @ExceptionHandler(HystrixRuntimeException.class)
  public final ResponseEntity<Object> handleHystrixRuntimeException(HystrixRuntimeException ex) {

    Throwable fallbackException = ex.getFallbackException();

    if (fallbackException != null) {

      Throwable fallbackCause = fallbackException.getCause();
      if (fallbackCause instanceof OneplatformException) {
        return CommonControllerAdvice.createResponseEntity((OneplatformException) fallbackCause);
      }
    }

    logger.error(String.format("Caught hystrix runtime exception: %s", ex.getMessage()));
    OneplatformException ope = InternalErrorException.builder().internalError().cause(ex).build();
    return CommonControllerAdvice.createResponseEntity(ope);
  }

}
