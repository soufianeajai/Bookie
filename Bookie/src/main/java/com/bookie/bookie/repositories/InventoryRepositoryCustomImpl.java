package com.bookie.bookie.repositories;

import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Hotel> findWithDynamicAvailability(HotelSearchCriteria criteria, Pageable pageable) {
        
        if (criteria.getStartDate() == null || criteria.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are mandatory for availability search.");
        }

        StringBuilder jpql = new StringBuilder(
            "SELECT DISTINCT i.hotel " +
            "FROM Inventory i JOIN i.room r " +
            "WHERE 1=1 "
        );

        Map<String, Object> parameters = new HashMap<>();

        jpql.append("AND i.date BETWEEN :startDate AND :endDate ");
        parameters.put("startDate", criteria.getStartDate());
        parameters.put("endDate", criteria.getEndDate());
        
        jpql.append("AND i.closed = false ");
        
        if (criteria.getCity() != null) {
            jpql.append("AND i.city = :city ");
            parameters.put("city", criteria.getCity());
        }

        if (criteria.getRoomType() != null) {
            jpql.append("AND r.type = :roomType ");
            parameters.put("roomType", criteria.getRoomType());
        }

        if (criteria.getRoomsCount() != null) {
            jpql.append("AND (i.totalCount - i.bookedCount) >= :roomsCount ");
            parameters.put("roomsCount", criteria.getRoomsCount());
        }

        jpql.append("GROUP BY i.hotel, r.id ");

        long dateCount = ChronoUnit.DAYS.between(criteria.getStartDate(), criteria.getEndDate()) + 1;
        jpql.append("HAVING COUNT(i.date) = :dateCount ");
        parameters.put("dateCount", dateCount);
        
        TypedQuery<Hotel> query = em.createQuery(jpql.toString(), Hotel.class);
        
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Hotel> hotels = query.getResultList();

        long totalElements = countTotalMatchingHotels(jpql.toString(), parameters);

        return new PageImpl<>(hotels, pageable, totalElements);
    }


    private long countTotalMatchingHotels(String dataJpql, Map<String, Object> parameters) {

        String countJpql = "SELECT COUNT(DISTINCT h.id) FROM (" +
                          dataJpql.replaceFirst("SELECT DISTINCT i.hotel", "SELECT DISTINCT i.hotel.id AS id") +
                          ") h";

        TypedQuery<Long> query = em.createQuery(countJpql, Long.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getSingleResult();
    }
}