package com.sharedservices.exception;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeoutException;

@RefreshScope
@RestController
public class StandaloneCreditController {

  private boolean isHystrixTimeout(Throwable exception) {
    return exception instanceof HystrixRuntimeException && exception.getCause() instanceof TimeoutException
        || exception != null && isHystrixTimeout(exception.getCause());
  }

}
