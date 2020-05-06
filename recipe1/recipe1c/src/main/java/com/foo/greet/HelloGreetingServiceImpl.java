package com.foo.greet;

import org.springframework.stereotype.Component;

@Component("englishGreeting")
public class HelloGreetingServiceImpl implements GreetingService {
    @Override
    public String greet() {
        return "Hello";
    }
}
