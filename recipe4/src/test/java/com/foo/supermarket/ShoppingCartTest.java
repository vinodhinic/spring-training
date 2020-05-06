package com.foo.supermarket;


import com.foo.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class ShoppingCartTest {

    @Autowired
    private ShoppingCart shoppingCart;

    @Test(expected = RuntimeException.class)
    public void testNegativeCase() {
        shoppingCart.addProduct(new Product("candy", 9.0));
        shoppingCart.checkout();
        shoppingCart.addProduct(new Product("wallclock", 100.0));
    }
}
