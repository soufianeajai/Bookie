package com.bookie.bookie.strategy;

import com.bookie.bookie.entities.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);
}
