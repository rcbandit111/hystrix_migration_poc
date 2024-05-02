package com.sharedservices.endpoint;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import com.sharedservices.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@DefaultProperties(ignoreExceptions = {BadRequestException.class, MethodArgumentNotValidException.class})
public class AccountController {

  @PostMapping("test")
  @HystrixCommand(groupKey = "accountMgmt", commandKey = "accountMgmt", threadPoolKey = "accountMgmt")
  public ResponseEntity<?> createAccounts(@PathVariable String resources) {
    ///....
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
