package com.bookie.bookie.entities;

import com.bookie.bookie.entities.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.control.MappingControl;

import java.util.Set;

@Entity
@Table(name = "guests")
@Getter
@Setter
public class Guest {
    @Id
    @SequenceGenerator(name = "guest_seq", sequenceName = "guest_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guest_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "guests_user_fk"))
    private User user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToMany(mappedBy = "guests")
    private Set<Booking> bookings;
}
