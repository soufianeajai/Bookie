package com.bookie.bookie.entities;

import com.bookie.bookie.entities.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter @Setter
public class Payment extends AuditableBase{
    @Id
    @SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true)
    private String transactionId;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", foreignKey = @ForeignKey(name = "payments_bookings_fk"))
    private Booking booking;
}
