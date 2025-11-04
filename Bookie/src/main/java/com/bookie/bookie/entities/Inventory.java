package com.bookie.bookie.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventories",
        uniqueConstraints = @UniqueConstraint(
                name = "unique_hotel_room_date",
                columnNames = {"hotel_id", "room_id", "date"}
        ))
@Getter
@Setter
public class Inventory extends AuditableBase {
    @Id
    @SequenceGenerator(name = "inventory_seq", sequenceName = "inventory_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false, foreignKey = @ForeignKey(name = "inventories_hotel_fk"))
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "inventories_room_fk"))
    private Room room;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "booked_count", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    @Column(name = "surge_factor", nullable = false, precision = 5, scale = 2)
    private BigDecimal surgeFactor;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price; // basePrice * surgeFactor

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "closed", nullable = false)
    private Boolean closed;
}
