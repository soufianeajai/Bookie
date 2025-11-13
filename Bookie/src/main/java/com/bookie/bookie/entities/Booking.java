package com.bookie.bookie.entities;


import com.bookie.bookie.entities.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "bookings")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends  AuditableBase{
    @Id
    @SequenceGenerator(name = "booking_seq", sequenceName = "booking_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false, foreignKey = @ForeignKey(name = "bookings_hotel_fk"))
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false, foreignKey = @ForeignKey(name = "bookings_room_fk"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "bookings_user_fk"))
    private User user;

    @Column(name = "rooms_count")
    private Integer roomsCount;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "bookings_guest",
                joinColumns = @JoinColumn(name = "booking_id"),
                inverseJoinColumns = @JoinColumn(name = "guest_id"),
                foreignKey = @ForeignKey(name = "bookings_guest_fk"),
                inverseForeignKey = @ForeignKey(name = "guests_booking_fk"))
    private Set<Guest> guests;


}
