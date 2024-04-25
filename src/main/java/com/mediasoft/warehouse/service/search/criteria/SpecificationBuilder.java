package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.service.search.OperationType;
import com.mediasoft.warehouse.service.search.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SpecificationBuilder {

    private final Map<OperationType, PredicateBuilder> predicateBuilders = Stream.of(
            new AbstractMap.SimpleEntry<>(OperationType.LIKE, new LikePredicateBuilder()),
            new AbstractMap.SimpleEntry<>(OperationType.EQUAL, new EqPredicateBuilder()),
            new AbstractMap.SimpleEntry<>(OperationType.GREATER_THAN_OR_EQ, new GTOEPredicateBuilder()),
            new AbstractMap.SimpleEntry<>(OperationType.LESS_THAN_OR_EQ, new LTOEPredicateBuilder())
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


    private Predicate buildPredicate(Root<Product> root, CriteriaBuilder cb, SearchCriteria<?> criterion) {
        return predicateBuilders.get(criterion.getOperation())
                .getPredicate(cb, root, criterion);
    }

    public Specification<Product> getSpecification(List<SearchCriteria<?>> searchCriteriaList) {
        Specification<Product> spec = Specification.where(null);
        for (var searchCriteria : searchCriteriaList) {
            spec = spec.and((root, query, cb) -> buildPredicate(root, cb, searchCriteria));
        }
        return spec;
    }
}