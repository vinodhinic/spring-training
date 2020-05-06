package com.foo;

import com.foo.message.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
        MessageService messageService = (MessageService) context.getBean("messageService");
        System.out.println(messageService.getMessage());
    }
}
