package com.sharedservices.endpoint;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.*;

@RestController
public class Endpoint {

    @HystrixCommand(groupKey = "group1", commandKey = "createAccount", threadPoolKey = "priority-1-pool",
            ignoreExceptions = {BadRequestException.class})
    @GetMapping("/greeting")
    public @ResponseBody String greeting()
    {
        try {
            throw new Exception("test");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        return "test response";
    }

}
