package com.sharedservices.endpoint;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointSec {

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

    @CircuitBreaker(name = "createAccount", fallbackMethod = "fallbackGetListValues")
    @GetMapping("/greetingSec")
    public ResponseEntity<?> greetingSec() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> fallbackGetListValues(Exception e) {
        return ResponseEntity.status(HttpStatus.OK).body("Fallback response");
    }

}
