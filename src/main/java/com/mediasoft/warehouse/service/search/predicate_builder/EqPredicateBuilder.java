package com.mediasoft.warehouse.service.search.predicate_builder;

import com.mediasoft.warehouse.model.enums.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class EqPredicateBuilder implements PredicateBuilder{

    @Override
    public Predicate getPredicateString(CriteriaBuilder cb, Expression<String> expression, String value) {
        return cb.equal(expression, value);
    }

    @Override
    public Predicate getPredicateInteger(CriteriaBuilder cb, Expression<Integer> expression, Integer value) {
        return cb.equal(expression, value);
    }

    @Override
    public Predicate getPredicateDate(CriteriaBuilder cb, Expression<LocalDate> expression, LocalDate value) {
        return cb.equal(cb.function("date", LocalDate.class, expression), value);
    }

    @Override
    public Predicate getPredicateBigDecimal(CriteriaBuilder cb, Expression<BigDecimal> expression, BigDecimal value) {
        return cb.equal(expression, value);
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
