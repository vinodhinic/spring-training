package com.foo.message;

import com.foo.greet.GreetingService;

public class MessageService {
    private GreetingService greetingService;

    public MessageService() {
        super();
    }

    public MessageService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public GreetingService getGreetingService() {
        return greetingService;
    }

    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String getMessage() {
        return greetingService.greet();
    }
}
