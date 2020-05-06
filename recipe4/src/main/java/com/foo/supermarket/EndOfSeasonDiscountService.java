package com.foo.supermarket;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("eos")
public class EndOfSeasonDiscountService implements DiscountService {

    @PostConstruct
    public void init() {
        System.out.println("Initializing EndOfSeasonDiscountService");
    }

    @Override
    public Double applyDiscount(Product product) {
        return product.getPrice() - product.getPrice() * 0.50;
    }
}
