package com.bookie.bookie.entities;


import com.bookie.bookie.entities.embeddable.HotelContactInfo;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Type;


import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
public class Hotel extends AuditableBase {
    @Id
    @SequenceGenerator(name = "hotel_seq", sequenceName = "hotel_seq", allocationSize = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotel_seq")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "city", nullable = false)
    private String city;

    @Type(ListArrayType.class)
    @Column(name = "photos", columnDefinition = "_text")
    private List<String> photos;

    @Type(ListArrayType.class)
    @Column(name = "amenities", columnDefinition = "_text")
    private List<String> amenities;

    @Embedded
    private HotelContactInfo contactInfo;

    @Column(nullable = false)
    private Boolean active;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "hotel", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Inventory> inventories;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true, foreignKey = @ForeignKey(name = "hotels_user_fk"))
    private User owner;
}
