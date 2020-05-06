package com.foo.supermarket;


import com.foo.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("default")
@ContextConfiguration(classes = AppConfig.class)
public class NullDiscountShoppingCartTest {

    @Autowired
    private ShoppingCart shoppingCart;

    @Test
    public void testShoppingCartForNullDiscounts() {
        shoppingCart.addProduct(new Product("mentos", 2.00));
        shoppingCart.addProduct(new Product("coke", 12.00));
        Double checkout = shoppingCart.checkout();
        assertEquals(checkout, Double.valueOf(14.00d));
    }
}
