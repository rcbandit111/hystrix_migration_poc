package com.sharedservices.endpoint;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

@RestController
public class Endpoint {

//    @HystrixCommand(groupKey = "group1", commandKey = "createAccount", threadPoolKey = "priority-1-pool",
//            ignoreExceptions = {BadRequestException.class})
//    @GetMapping("/greeting")
//    public @ResponseBody String greeting()
//    {
//        try {
//            throw new Exception("test");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
////        return "test response";
//    }

    @TimeLimiter(name = "createAccount", fallbackMethod = "fallbackGetListValueTimeout")
    @CircuitBreaker(name = "createAccount", fallbackMethod = "fallbackGetListValues")
    @GetMapping("/greeting")
    public ResponseEntity<?> greeting() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> fallbackGetListValues(Exception e) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.OK).body("Fallback response");
    }

    private CompletableFuture<?> fallbackGetListValueTimeout(Exception e){
        if(e instanceof TimeoutException){
            System.out.println("TimeoutException");
        }
        return null;
    }

}
