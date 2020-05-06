package com.foo;

import com.foo.config.AppConfig;
import com.foo.supermarket.Product;
import com.foo.supermarket.ShoppingCart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ShoppingCart shoppingCart1 = context.getBean(ShoppingCart.class);
        shoppingCart1.addProduct(new Product("biscuit"));
        shoppingCart1.addProduct(new Product("chocolate"));

        ShoppingCart shoppingCart2 = context.getBean(ShoppingCart.class);
        shoppingCart2.addProduct(new Product("vegetables"));
        shoppingCart2.addProduct(new Product("fruits"));

        System.out.println(shoppingCart1);
        System.out.println(shoppingCart2);

        // you would never do this ideally. Just showing how the preDestroy() is called when the bean is evicted.
        // generally resource cleanup - like closing a file - goes under preDestroy
        context.getAutowireCapableBeanFactory().destroyBean(shoppingCart1);
        context.getAutowireCapableBeanFactory().destroyBean(shoppingCart2);
    }
}
