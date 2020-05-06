package com.foo.supermarket;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("flash")
// note that @Component, @Service, @Repository all does the same thing. but we annotate as per the application layer we are working on.
public class FlashSaleDiscountService implements DiscountService {

    @PostConstruct
    public void init() {
        System.out.println("Initializing FlashSaleDiscountService");
    }

    @Override
    public Double applyDiscount(Product product) {
        return product.getPrice() - product.getPrice() * 0.20;
    }
}
