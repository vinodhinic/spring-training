package com.foo;


import com.foo.config.AppConfig;
import com.foo.message.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * Go to app-test.properties and delete the entry.
 * Now name would be read from app.properties
 * Go to app.properties and delete the entry from there as well.
 * name would fallback to default value "world"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test-app.properties")
@ContextConfiguration(classes = AppConfig.class)
public class MessageServiceTest2 {

    @Autowired
    private MessageService messageService;

    @Test
    public void getMessage() {
        String msg = messageService.getMessage();
        assertEquals("Hello test", msg);
    }
}