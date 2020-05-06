package com.foo.supermarket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Scope("prototype")
public class ShoppingCart {

    private List<Product> products = new ArrayList<>();
    private Long uniqueId;

    public void addProduct(Product product) {
        products.add(product);

    }

    static Random random = new Random();

    @PostConstruct
    public void init() {
        uniqueId = random.nextLong();
        System.out.println("Shopping cart bean is initialized. ID :" + uniqueId);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Shopping card bean is destroyed. ID : " + uniqueId);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "products=" + products +
                '}';
    }
}
