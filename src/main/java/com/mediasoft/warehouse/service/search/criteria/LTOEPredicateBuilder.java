package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class LTOEPredicateBuilder implements PredicateBuilder{
    @Override
    public Predicate getPredicateString(CriteriaBuilder cb, Expression<String> expression, String value) {
        return cb.lessThanOrEqualTo(cb.length(expression), value.length());
    }

    @Override
    public Predicate getPredicateInteger(CriteriaBuilder cb, Expression<Integer> expression, Integer value) {
        return cb.lessThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getPredicateDate(CriteriaBuilder cb, Expression<LocalDate> expression, LocalDate value) {
        return cb.lessThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getPredicateBigDecimal(CriteriaBuilder cb, Expression<BigDecimal> expression, BigDecimal value) {
        return cb.lessThanOrEqualTo(expression, value);
    }

    @Override
    public Predicate getPredicateCategory(CriteriaBuilder cb, Expression<Category> expression, Category value) {
        return cb.equal(expression, value);
    }

    @Override
    public Predicate getPredicateUUID(CriteriaBuilder cb, Expression<UUID> expression, UUID value) {
        return cb.lessThanOrEqualTo(expression, value);
    }
}
