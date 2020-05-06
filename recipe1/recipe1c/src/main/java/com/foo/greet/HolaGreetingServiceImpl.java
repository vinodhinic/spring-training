package com.foo.greet;

import org.springframework.stereotype.Component;

@Component("spanishGreeting")
public class HolaGreetingServiceImpl implements GreetingService {
    @Override
    public String greet() {
        return "Hola";
    }
}
