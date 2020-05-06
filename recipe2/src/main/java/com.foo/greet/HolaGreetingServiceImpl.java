package com.foo.greet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("spanishGreeting")
public class HolaGreetingServiceImpl implements GreetingService {
    @Autowired
    @Qualifier("name")
    private String name;

    @Override
    public String greet() {
        return "Hola " + name;
    }
}
