package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;

import org.springframework.http.ResponseEntity;

/**
 * Exception handler for hystrix specific errors.
 */
public class HystrixExceptionHandler extends ExceptionHandlerNode {

  @Override
  public ResponseEntity<ErrorResultResource> handleException(Exception exception) {

    if (exception instanceof HystrixRuntimeException) {
      return handleHystrixRuntimeException((HystrixRuntimeException) exception);
    }

    return nextHandleException(exception);
  }

  /**
   * Handles hystrix runtime exceptions with consistent responses and details.
   *
   * @param ex an unexpected exception
   * @return a standard ErrorResult
   */
  private ResponseEntity<ErrorResultResource> handleHystrixRuntimeException(HystrixRuntimeException ex) {

    Throwable fallbackException = ex.getFallbackException();

    if (fallbackException != null) {

      Throwable fallbackCause = fallbackException.getCause();
      if (fallbackCause instanceof OneplatformException) {
        return ResponseEntityCreator.createResponseEntity((OneplatformException) fallbackCause);
      }
    }

    logger.error("Caught hystrix runtime exception: {}", ex.getMessage());
    OneplatformException ope = InternalErrorException.builder()
        .internalError()
        .cause(ex)
        .build();

    return ResponseEntityCreator.createResponseEntity(ope);
  }

}
