package com.mediasoft.warehouse;

import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.search.OperationType;
import com.mediasoft.warehouse.service.search.SearchCriteria;
import com.mediasoft.warehouse.service.search.SearchCriteria.BigDecimalSearchCriteria;
import com.mediasoft.warehouse.service.search.SearchCriteria.DateSearchCriteria;
import com.mediasoft.warehouse.service.search.SearchCriteria.StringSearchCriteria;
import com.mediasoft.warehouse.service.search.SearchCriteria.IntegerSearchCriteria;

import com.mediasoft.warehouse.service.search.criteria.SpecificationBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Sql(scripts = "classpath:/sql/create-products.sql")
public class ProductSearchDataJpaTest {
    @Autowired
    private ProductRepository productRepository;
    private final SpecificationBuilder specificationBuilder = new SpecificationBuilder();
    @Test
    public void quantityEqualTest() {
        Integer expectedQuantity = 2;
        IntegerSearchCriteria integerSearchCriteria = new IntegerSearchCriteria(
                expectedQuantity,
                "quantity",
                OperationType.EQUAL);

        List<SearchCriteria<?>> searchCriteriaList = List.of(integerSearchCriteria);
        Specification<Product> specification = specificationBuilder.getSpecification(searchCriteriaList);
        List<Product> actualProducts = productRepository.findAll(specification);
        Assertions.assertThat(actualProducts)
                .anySatisfy(product -> assertEquals(expectedQuantity, product.getQuantity()))
                .hasSize(3);
    }

    @Test
    public void multiCriteriaTest() {
        String nameLike = "product";
        BigDecimal priceGt = new BigDecimal("110.0");
        BigDecimal priceLt = new BigDecimal("240.1");
        LocalDate localDate = LocalDate.of(2024, 9,20);

        StringSearchCriteria stringSearchCriteria = new StringSearchCriteria(
                nameLike,
                "name",
                OperationType.LIKE);
        BigDecimalSearchCriteria bigDecimalSearchCriteriaGt = new BigDecimalSearchCriteria(
                priceGt,
                "price",
                OperationType.GREATER_THAN_OR_EQ
        );
        BigDecimalSearchCriteria bigDecimalSearchCriteriaLt = new BigDecimalSearchCriteria(
                priceLt,
                "price",
                OperationType.LESS_THAN_OR_EQ
        );
       DateSearchCriteria dateSearchCriteria = new DateSearchCriteria(
                localDate,
                "createdDate",
                OperationType.GREATER_THAN_OR_EQ
        );

        List<SearchCriteria<?>> searchCriteriaList = List.of(stringSearchCriteria,
                bigDecimalSearchCriteriaGt, bigDecimalSearchCriteriaLt, dateSearchCriteria);

        Specification<Product> specification = specificationBuilder.getSpecification(searchCriteriaList);
        List<Product> actualProducts = productRepository.findAll(specification);
        Assertions.assertThat(actualProducts)
                .anySatisfy(product -> {
                    assertTrue(product.getName().contains(nameLike));
                    assertTrue(product.getPrice().compareTo(priceGt) >= 0);
                    assertTrue(product.getPrice().compareTo(priceLt) <= 0);
                    assertTrue(product.getCreatedDate().isAfter(localDate.atStartOfDay())
                            || product.getCreatedDate().isEqual(localDate.atStartOfDay()));
                })
                .hasSize(1);
    }
}
