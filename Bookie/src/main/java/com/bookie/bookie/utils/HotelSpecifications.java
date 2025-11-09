package com.bookie.bookie.utils;

import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.entities.Hotel;
import com.bookie.bookie.entities.Inventory;
import com.bookie.bookie.entities.Room;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class HotelSpecifications {

    private HotelSpecifications() {
    }

    private static Specification<Hotel> empty() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> cb.equal(root.get("city"), city);
    }

    public static Specification<Hotel> hasRoomType(String roomType) {
        return (root, query, criteriaBuilder) -> {
            Join<Hotel, Room> hotelRoomJoin = root.join("rooms");
            return criteriaBuilder.equal(hotelRoomJoin.get("type"), roomType);
        };
    }

    public static Specification<Hotel> hasAvailableInventory(
            LocalDate startDate, LocalDate endDate, String roomType, Integer roomsCount) {

        return (root, query, cb) -> {
            assert (query != null);
            query.distinct(true);

            Join<Hotel, Inventory> inventoryJoin = root.join("inventories", JoinType.INNER);
            Join<Inventory, Room> roomJoin = inventoryJoin.join("room", JoinType.INNER);
            Predicate datePredicate = cb.between(inventoryJoin.get("date"), startDate, endDate);
            Predicate availabilityPredicate = cb.lessThan(inventoryJoin.get("bookedCount"), inventoryJoin.get("totalCount"));
            Predicate finalPredicate = cb.and(datePredicate, availabilityPredicate);
            if (roomType != null) {
                Predicate roomTypePredicate = cb.equal(roomJoin.get("type"), roomType);
                finalPredicate = cb.and(finalPredicate, roomTypePredicate);
            }
            if (roomsCount != null) {
                Expression<Integer> totalCountExpr = inventoryJoin.get("totalCount");
                Expression<Integer> bookedCountExpr = inventoryJoin.get("bookedCount");
                Expression<Integer> availableCountExpr = cb.diff(totalCountExpr, bookedCountExpr);
                Predicate roomsAvailabilityPredicate = cb.greaterThanOrEqualTo(availableCountExpr, roomsCount);
                finalPredicate = cb.and(finalPredicate, roomsAvailabilityPredicate);
            }
            return finalPredicate;
        };
    }

    public static Specification<Hotel> withCriteria(HotelSearchCriteria criteria) {
        Specification<Hotel> spec = empty();
        if (criteria.getCity() != null) {
            spec = spec.and(hasCity(criteria.getCity()));
        }
        if (criteria.getRoomType() != null) {
            spec = spec.and(hasRoomType(criteria.getRoomType()));
        }
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            spec = spec.and(hasAvailableInventory(criteria.getStartDate(), criteria.getEndDate(), criteria.getRoomType(), criteria.getRoomsCount()));
        }
        return spec;
    }
}
