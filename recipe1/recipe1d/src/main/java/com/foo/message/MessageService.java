package com.foo.message;

import com.foo.greet.GreetingService;

public class MessageService {

    private GreetingService greetingService;

    public MessageService(GreetingService service) {
        this.greetingService = service;
    }

    public String getMessage() {
        return greetingService.greet();
    }
}