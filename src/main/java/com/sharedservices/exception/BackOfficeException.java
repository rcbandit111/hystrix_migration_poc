package com.sharedservices.exception;

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix;

import java.util.List;

import lombok.Getter;

/**
 * BackOfficeException.class.
 */
@Getter
public class BackOfficeException extends RuntimeException implements ExceptionNotWrappedByHystrix {

  private final Integer status;
  private final String methodKey;
  private final String errorCode;
  private final String errorMessage;
  private final String errorDescription;
  private final List<String> details;

  public BackOfficeException(String errorCode, String errorMessage, String errorDescription, Throwable throwable,
      Integer status) {
    super(errorMessage, throwable);
    this.status = status;
    this.methodKey = null;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errorDescription = errorDescription;
    this.details = null;
  }
}
