package com.sharedservices.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "You got a invalid request."
)
public class BadRequestException extends Exception{
}
