package com.foo;

import com.foo.config.AppConfig;
import com.foo.message.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MessageService messageService = (MessageService) context.getBean("messageService");
        System.out.println(messageService.getMessage());
    }
}
