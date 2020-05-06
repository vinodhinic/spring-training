package com.foo.message;

import com.foo.greet.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    @Qualifier("englishGreeting")
    @Autowired
    private GreetingService greetingService;

    public String getMessage() {
        return greetingService.greet();
    }
}