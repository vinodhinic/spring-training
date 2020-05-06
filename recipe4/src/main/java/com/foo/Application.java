package com.foo;

import com.foo.config.AppConfig;
import com.foo.supermarket.Product;
import com.foo.supermarket.ShoppingCart;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
         /*
        Note that context use to be initialized like this:
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        I have changed it now. Understand the reason and difference.
         */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("eos"); // remove this and observe the default profile is "default"
        // also change this to "flash"
        context.register(AppConfig.class);
        context.refresh();

        ShoppingCart shoppingCart1 = context.getBean(ShoppingCart.class);
        shoppingCart1.addProduct(new Product("biscuit", 10.90));
        shoppingCart1.addProduct(new Product("chocolate", 11.10));

        ShoppingCart shoppingCart2 = context.getBean(ShoppingCart.class);
        shoppingCart2.addProduct(new Product("vegetables", 90.00));
        shoppingCart2.addProduct(new Product("fruits", 100.00));

        System.out.println(shoppingCart1);
        System.out.println(shoppingCart2);

        System.out.println(shoppingCart1.checkout());
        System.out.println(shoppingCart2.checkout());

        // you would never do this ideally. Just showing how the preDestroy() is called when the bean is evicted.
        // generally resource cleanup - like closing a file - goes under preDestroy
        context.getAutowireCapableBeanFactory().destroyBean(shoppingCart1);
        context.getAutowireCapableBeanFactory().destroyBean(shoppingCart2);
    }
}
