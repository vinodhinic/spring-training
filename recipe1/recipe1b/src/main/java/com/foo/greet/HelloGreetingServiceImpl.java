package com.foo.greet;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class HelloGreetingServiceImpl implements GreetingService {
    @Override
    public String greet() {
        return "Hello";
    }
}
