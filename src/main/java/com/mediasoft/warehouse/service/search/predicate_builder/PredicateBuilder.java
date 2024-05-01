package com.mediasoft.warehouse.service.search.predicate_builder;


import com.mediasoft.warehouse.model.Category;
import com.mediasoft.warehouse.service.search.criteria.BigDecimalSearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.CategorySearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.DateSearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.IntegerSearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.SearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.StringSearchCriteria;
import com.mediasoft.warehouse.service.search.criteria.UUIDSearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface PredicateBuilder {

    Predicate getPredicateString(CriteriaBuilder cb, Expression<String> expression, String value);

    Predicate getPredicateInteger(CriteriaBuilder cb, Expression<Integer> expression, Integer value);

    Predicate getPredicateDate(CriteriaBuilder cb, Expression<LocalDate> expression, LocalDate value);

    Predicate getPredicateBigDecimal(CriteriaBuilder cb, Expression<BigDecimal> expression, BigDecimal value);

    Predicate getPredicateCategory(CriteriaBuilder cb, Expression<Category> expression, Category value);

    Predicate getPredicateUUID(CriteriaBuilder cb, Expression<UUID> expression, UUID value);

    default Predicate getPredicate(CriteriaBuilder cb, Path<?> path, SearchCriteria<?> searchCriteria) {
        var field = searchCriteria.getField();
        var value = searchCriteria.getValue();
        if (StringSearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateString(cb, path.get(field), (String) value);
        }
        if (IntegerSearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateInteger(cb, path.get(field), (Integer) value);
        }
        if (DateSearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateDate(cb, path.get(field), (LocalDate) value);
        }
        if (BigDecimalSearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateBigDecimal(cb, path.get(field), (BigDecimal) value);
        }
        if (CategorySearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateCategory(cb, path.get(field), (Category) value);
        }
        if (UUIDSearchCriteria.class.equals(searchCriteria.getClass())) {
            return getPredicateUUID(cb, path.get(field), (UUID) value);
        }
        return null;
    }
}
