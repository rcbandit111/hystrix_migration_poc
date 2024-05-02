package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class RestTemplateHystrixService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateHystrixService.class);

  private final String group;
  private final String command;
  private final String threadPool;
  private final RestTemplateService restClient;

  /**
   * Constructor for hystrix rest template service.
   *
   * @param group hystrix group name
   * @param command hxstrix command name
   * @param threadPool hystrix thread poolname
   * @param restClient rest client to be used internally
   */
  public RestTemplateHystrixService(String group, String command, String threadPool,
      RestTemplateService restClient) {
    this.group = group;
    this.command = command;
    this.threadPool = threadPool;
    this.restClient = restClient;
  }

  /**
   * Wraps the call with a call to restTemplateRetry; which will retry in the event it could not reach the resource.
   */
  public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
      ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    try {

      Command<T> cmd = new Command.Builder<T>(group, command, threadPool)
          .restClient(restClient).url(url).method(method).requestEntity(requestEntity)
          .responseType(responseType).uriVariables(uriVariables)
          .build();

      return cmd.execute();
    } catch (HystrixBadRequestException | HystrixRuntimeException e) {
      if (e.getCause() instanceof RuntimeException) {
        LOGGER.debug("Removing hystrix wrapper Exception.", e);
        throw (RuntimeException) e.getCause();
      }
      throw e;
    }
  }
}
