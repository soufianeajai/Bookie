package com.bookie.bookie.services.impl;

import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.HotelMinPrice;
import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.repositories.HotelMinPriceRepository;
import com.bookie.bookie.repositories.HotelRepository;
import com.bookie.bookie.repositories.InventoryRepository;
import com.bookie.bookie.strategy.PricingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PricingUpdateService {

    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void updatePrices(){
        int page = 0;
        int batchSize = 100;

        while (true){
            Page<Hotel> hotelsPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
            if (hotelsPage.isEmpty()) break;
            hotelsPage.forEach(this::updateHotelPrices);
            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel){
        log.info("updating the hotel prices for hotel {}", hotel.getId());
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        List<Inventory> inventories = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);
        updateInventoryPrices(inventories);
        updateHotelMinPrice(hotel, inventories, startDate, endDate);
    }

    private void updateInventoryPrices(List<Inventory> inventories){
        log.info("updating the inventory prices");

        inventories.forEach((inventory -> inventory.setPrice(pricingService.calculateDynamicPricing(inventory))));
        inventoryRepository.saveAll(inventories);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventories, LocalDate startDate, LocalDate endDate){
        Map<LocalDate, BigDecimal> dailyMinPrices = inventories.stream().collect(Collectors.groupingBy(Inventory::getDate, Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder())))).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().orElse(BigDecimal.ZERO)));
        List<HotelMinPrice> hotelPrices = new ArrayList<>();
        dailyMinPrices.forEach((date, price) -> {
            HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel, date).orElse(new HotelMinPrice(hotel, date));
            hotelMinPrice.setPrice(price);
            hotelPrices.add(hotelMinPrice);
        });

        hotelMinPriceRepository.saveAll(hotelPrices);
    }
}
