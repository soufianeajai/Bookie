package com.bookie.bookie.strategy;

import com.bookie.bookie.entities.Inventory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
public class SurgePricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapper;


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapper.calculatePrice(inventory);
        return price.multiply(inventory.getSurgeFactor());
    }
}
