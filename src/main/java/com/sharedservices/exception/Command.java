package com.sharedservices.exception;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

public final class Command<T> extends HystrixCommand<ResponseEntity<T>> {

  private final RestTemplateService restClient;
  private final String url;
  private final HttpMethod method;
  private final HttpEntity<?> requestEntity;
  private final ParameterizedTypeReference<T> responseType;
  private final Object[] uriVariables;

  /**
   * Constructor for the command.
   *
   * @param group given group
   * @param command given command
   * @param threadPool given thread pool
   * @param builder given builder to build the object
   */
  public Command(String group, String command, String threadPool, Builder<T> builder) {
    super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group))
        .andCommandKey(HystrixCommandKey.Factory.asKey(command))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(threadPool)));

    this.restClient = builder.restClient;
    this.url = builder.url;
    this.method = builder.method;
    this.requestEntity = builder.requestEntity;
    this.responseType = builder.responseType;
    this.uriVariables = builder.uriVariables;
  }

  /**
   * Builder for {@link Command}.
   */
  public static class Builder<T> {

    private RestTemplateService restClient;
    private String url;
    private HttpMethod method;
    private HttpEntity<?> requestEntity;
    private ParameterizedTypeReference<T> responseType;
    private Object[] uriVariables;

    private final String group;
    private final String command;
    private final String threadPool;

    /**
     * Constructor for the builder.
     *
     * @param group the hystrix group key
     * @param command the hystrix command key
     * @param threadPool the hystrix thread pool key
     */
    public Builder(String group, String command, String threadPool) {
      this.group = group;
      this.command = command;
      this.threadPool = threadPool;
    }

    public Builder<T> url(String url) {
      this.url = url;
      return this;
    }

    public Builder<T> restClient(RestTemplateService restClient) {
      this.restClient = restClient;
      return this;
    }

    public Builder<T> method(HttpMethod method) {
      this.method = method;
      return this;
    }

    public Builder<T> requestEntity(HttpEntity<?> requestEntity) {
      this.requestEntity = requestEntity;
      return this;
    }

    public Builder<T> responseType(ParameterizedTypeReference<T> responseType) {
      this.responseType = responseType;
      return this;
    }

    public Builder<T> uriVariables(Object... uriVariables) {
      this.uriVariables = uriVariables;
      return this;
    }

    public Command<T> build() {
      return new Command<>(group, command, threadPool, this);
    }
  }

  @Override
  protected ResponseEntity<T> run() throws Exception {
    try {
      return restClient.exchange(url, method, requestEntity, responseType, uriVariables);
    } catch (HttpStatusCodeException hsce) {
      // 500's are an error. We use 402 for a decline, so not an error.
      // A HystrixBadRequestException is a way to bypass hystrix; to not count this transaction
      // as a failure.
      if (!hsce.getStatusCode().is5xxServerError()) {
        throw new HystrixBadRequestException("override", hsce);
      }
      throw hsce;
    }
  }
}
