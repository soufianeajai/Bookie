package com.bookie.bookie.entities;


import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter @Setter
public class Room extends AuditableBase {

    @Id
    @SequenceGenerator(name = "room_seq", sequenceName = "room_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "hotel_id", nullable = false, foreignKey = @ForeignKey(name = "rooms_hotel_fk"))
    private Hotel hotel;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Type(ListArrayType.class)
    @Column(name = "photos", columnDefinition = "_text")
    private List<String> photos;

    @Type(ListArrayType.class)
    @Column(name = "amenities", columnDefinition = "_text")
    private List<String> amenities;

    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

}
