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

// ********* This is another way to do the same thing as above - called "constructor injection" *************
/*@Component
public class MessageService {

    private GreetingService greetingService;

    @Autowired
    public MessageService( @Qualifier("englishGreeting")  GreetingService service) {
        // ideally used if you require some other bean to instantiate this class.
        this.greetingService = service;
    }

    public String getMessage() {
        return greetingService.greet();
    }
}*/
