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

    private HotelSpecifications(){}

    private static Specification<Hotel> empty() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Hotel> hasCity(String city) {
        return (root, query, cb) -> cb.equal(root.get("city"), city);
    }

//    public static Specification<Hotel> hasRoomsCount(Integer count) {
//        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("roomsCount"), count);
//    }

    public static Specification<Hotel> hasRoomType(String roomType){
        return (root, query, criteriaBuilder) -> {
            Join<Hotel, Room> hotelRoomJoin = root.join("rooms");
            assert query != null;
            query.distinct(true);
            return criteriaBuilder.equal(hotelRoomJoin.get("type"), roomType);
        };
    }

    public static Specification<Hotel> hasAvailableInventory(LocalDate startDate, LocalDate endDate, Integer roomsCount) {
        return (root, query, cb) -> {
            var roomsAvailabilityPredicate = cb.conjunction();
            Join<Hotel, Inventory> inventoryJoin = root.join("inventories", JoinType.INNER);
            assert query != null;
            query.distinct(true);
            var datePredicate = cb.between(inventoryJoin.get("date"), startDate, endDate);
            var availabilityPredicate = cb.lessThan(inventoryJoin.get("bookedCount"), inventoryJoin.get("totalCount"));
            Predicate predicate = cb.and(datePredicate, availabilityPredicate);
            if (roomsCount != null){
                Expression<Integer> totalCountExpr = inventoryJoin.get("totalCount");
                Expression<Integer> bookedCountExpr = inventoryJoin.get("bookedCount");
                var availableCountExpr = cb.diff(totalCountExpr, bookedCountExpr);
                roomsAvailabilityPredicate = cb.greaterThanOrEqualTo(availableCountExpr, roomsCount);
            }
            return cb.and(predicate, roomsAvailabilityPredicate);
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
            spec = spec.and(hasAvailableInventory(criteria.getStartDate(), criteria.getEndDate(), criteria.getRoomsCount()));
        }
        return spec;
    }

}
