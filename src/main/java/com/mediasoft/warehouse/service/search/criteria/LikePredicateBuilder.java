package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class LikePredicateBuilder implements PredicateBuilder {

    @Override
    public Predicate getPredicateString(CriteriaBuilder cb, Expression<String> expression, String value) {
        return cb.like(cb.lower(expression), "%" + value.toLowerCase() + "%");
    }

    @Override
    public Predicate getPredicateInteger(CriteriaBuilder cb, Expression<Integer> expression, Integer value) {
        var valBefore = value + 1;
        var valAfter = value - 1;
        return cb.between(expression, valBefore, valAfter);
    }

    @Override
    public Predicate getPredicateDate(CriteriaBuilder cb, Expression<LocalDate> expression, LocalDate value) {
        var valBefore = value.minusDays(1);
        var valAfter = value.plusDays(1);
        return cb.between(expression, valBefore, valAfter);
    }

    @Override
    public Predicate getPredicateBigDecimal(CriteriaBuilder cb, Expression<BigDecimal> expression, BigDecimal value) {
        var increasedValue = value.multiply(new BigDecimal("1.10"));
        var decreasedValue = value.multiply(new BigDecimal("0.90"));
        return cb.and(cb.greaterThanOrEqualTo(expression, decreasedValue),
                cb.lessThanOrEqualTo(expression, increasedValue));
    }

    @Override
    public Predicate getPredicateCategory(CriteriaBuilder cb, Expression<Category> expression, Category value) {
        return cb.equal(expression, value);
    }

    @Override
    public Predicate getPredicateUUID(CriteriaBuilder cb, Expression<UUID> expression, UUID value) {
        return cb.equal(expression, value);
    }
}
