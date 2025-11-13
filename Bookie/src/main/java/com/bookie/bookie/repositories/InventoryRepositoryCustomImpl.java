package com.bookie.bookie.repositories;

import com.bookie.bookie.dtos.hotel.HotelSearchCriteria;
import com.bookie.bookie.entities.Hotel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Hotel> findWithDynamicAvailability(HotelSearchCriteria criteria, Pageable pageable) {
        Map<String, Object> parameters = new HashMap<>();
        StringBuilder subqueryJpql = new StringBuilder(
                "SELECT DISTINCT i.hotel.id " +
                        "FROM Inventory i " +
                        "JOIN i.room r " +
                        "WHERE 1=1 "
        );
        if (criteria.getStartDate() == null || criteria.getEndDate() == null) {
            throw new InvalidDataAccessApiUsageException("Start date and end date are mandatory for availability search.");
        }
        subqueryJpql.append("AND i.date BETWEEN :startDate AND :endDate ");
        parameters.put("startDate", criteria.getStartDate());
        parameters.put("endDate", criteria.getEndDate());
        subqueryJpql.append("AND i.closed = false ");

        if (criteria.getCity() != null) {
            subqueryJpql.append("AND i.city = :city ");
            parameters.put("city", criteria.getCity());
        }
        if (criteria.getRoomType() != null) {
            subqueryJpql.append("AND r.type = :roomType ");
            parameters.put("roomType", criteria.getRoomType());
        }
        if (criteria.getRoomsCount() != null) {
            subqueryJpql.append("AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomsCount ");
            parameters.put("roomsCount", criteria.getRoomsCount());
        }
        subqueryJpql.append("GROUP BY i.hotel.id, r.id ");
        long dateCount = ChronoUnit.DAYS.between(criteria.getStartDate(), criteria.getEndDate()) + 1;
        subqueryJpql.append("HAVING COUNT(i.date) = :dateCount ");
        parameters.put("dateCount", dateCount);
        StringBuilder mainJpql = new StringBuilder("SELECT h FROM Hotel h WHERE h.id IN (");
        mainJpql.append(subqueryJpql);
        mainJpql.append(")");
        if (pageable.getSort().isSorted()) {
            mainJpql.append(" ORDER BY ");
            List<String> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                String direction = order.getDirection().isAscending() ? "ASC" : "DESC";
                orders.add("h." + order.getProperty() + " " + direction);
            }
            mainJpql.append(String.join(", ", orders));
        }

        TypedQuery<Hotel> query = em.createQuery(mainJpql.toString(), Hotel.class);
        parameters.forEach(query::setParameter);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Hotel> hotels = query.getResultList();
        long totalElements = countTotalMatchingHotels(subqueryJpql.toString(), parameters);
        return new PageImpl<>(hotels, pageable, totalElements);
    }


    private long countTotalMatchingHotels(String subqueryJpql, Map<String, Object> parameters) {

        String simplerCountJpql = "SELECT COUNT(DISTINCT h.id) FROM Hotel h WHERE h.id IN (" +
                subqueryJpql + ")";
        TypedQuery<Long> query = em.createQuery(simplerCountJpql, Long.class);
        parameters.forEach(query::setParameter);

        return query.getSingleResult();
    }
}