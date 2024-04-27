package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.service.search.OperationType;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {
    @Positive
    BigDecimal value;
    String field;
    OperationType operation;
}
