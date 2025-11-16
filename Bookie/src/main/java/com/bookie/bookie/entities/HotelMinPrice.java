package com.bookie.bookie.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hotel_min_price")
@Getter
@Setter
@NoArgsConstructor
public class HotelMinPrice extends AuditableBase{
    @Id
    @SequenceGenerator(name = "hotel_seq", sequenceName = "hotel_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotel_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false, foreignKey = @ForeignKey(name = "hotel_min_price_hotel_fk", foreignKeyDefinition = "FOREIGN KEY (hotel_id) REFERENCES hotels (id) ON DELETE CASCADE"))
    private Hotel hotel;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // cheapest room price for a hotel

    public HotelMinPrice(Hotel hotel, LocalDate date){
        this.hotel = hotel;
        this.date = date;
    }

}
