package com.foo.greet;

public class HelloGreetingServiceImpl implements GreetingService {
    @Override
    public String greet() {
        return "Hello";
    }
}
