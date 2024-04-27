package com.mediasoft.warehouse.service.search;

import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.service.search.criteria.SearchCriteria;
import com.mediasoft.warehouse.service.search.predicate_builder.EqPredicateBuilder;
import com.mediasoft.warehouse.service.search.predicate_builder.GTOEPredicateBuilder;
import com.mediasoft.warehouse.service.search.predicate_builder.LTOEPredicateBuilder;
import com.mediasoft.warehouse.service.search.predicate_builder.LikePredicateBuilder;
import com.mediasoft.warehouse.service.search.predicate_builder.PredicateBuilder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SpecificationBuilder {

    private final Map<OperationType, PredicateBuilder> predicateBuilders = Map.of(
            OperationType.LIKE, new LikePredicateBuilder(),
            OperationType.EQUAL, new EqPredicateBuilder(),
            OperationType.GREATER_THAN_OR_EQ, new GTOEPredicateBuilder(),
            OperationType.LESS_THAN_OR_EQ, new LTOEPredicateBuilder()
    );


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