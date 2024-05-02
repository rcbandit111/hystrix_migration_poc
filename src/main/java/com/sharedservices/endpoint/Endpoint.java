package com.sharedservices.endpoint;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sharedservices.exception.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
public class Endpoint {

    @HystrixCommand(groupKey = "group1", commandKey = "createAccount", threadPoolKey = "priority-1-pool",
            ignoreExceptions = {BadRequestException.class})
    @GetMapping("/greeting")
    public @ResponseBody String greeting()
    {
        // some code implementation
        return "test response";
    }

}
