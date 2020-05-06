package com.foo.supermarket;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Profile("default")
@Service
public class NullDiscountService implements DiscountService {

    @PostConstruct
    public void init() {
        System.out.println("Initializing NullDiscountService");
    }

    @Override
    public Double applyDiscount(Product product) {
        return product.getPrice();
    }
}
