package com.foo.supermarket;

import org.springframework.beans.factory.annotation.Autowired;
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
    private boolean isCheckedOut = false;

    @Autowired
    private DiscountService discountService;

    public void addProduct(Product product) {
        if (this.isCheckedOut) {
            throw new RuntimeException("Cannot add product after the cart is checked out");
        } else {
            products.add(product);
        }
    }

    public Double checkout() {
        this.isCheckedOut = true;
        return products
                .stream()
                .map(p -> discountService.applyDiscount(p))
                .mapToDouble(e -> e)
                .sum();
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
