package com.foo;

import com.foo.config.AppConfig;
import com.foo.message.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void getMessage() {
        String msg = messageService.getMessage();
        assertEquals("Hello", msg);
    }
}