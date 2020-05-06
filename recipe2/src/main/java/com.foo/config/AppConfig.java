package com.foo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = "com.foo")
public class AppConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // since the application logic is to default to "world", injecting the name property directly at
    // the greeting service implementations would mean that - if you were to change this "world" to "somethingelse"
    // tomorrow, you would have to look at multiple places
    // For such cases, it is clever to create a bean and use the bean instead.
    @Bean(name = "name")
    public String name(@Value("${name:world}") String nameReadFromProps) {
        return nameReadFromProps;
    }
}
