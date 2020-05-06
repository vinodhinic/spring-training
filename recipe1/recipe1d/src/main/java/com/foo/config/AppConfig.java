package com.foo.config;

import com.foo.greet.GreetingService;
import com.foo.greet.HelloGreetingServiceImpl;
import com.foo.greet.HolaGreetingServiceImpl;
import com.foo.message.MessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(name = "englishGreeting")
    public GreetingService englishGreetingService() {
        return new HelloGreetingServiceImpl();
    }

    @Bean(name = "spanishGreeting")
    public GreetingService spanishGreetingService() {
        return new HolaGreetingServiceImpl();
    }

    @Bean
    public MessageService messageService() {
        return new MessageService(englishGreetingService());
    }

    /***************** This is another way**************************
     /* @Bean
     public MessageService messageService(@Qualifier("englishGreeting") GreetingService service){
     return new MessageService(service);
     }*/

}
