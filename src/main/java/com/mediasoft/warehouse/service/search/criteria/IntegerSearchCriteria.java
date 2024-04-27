package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.service.search.OperationType;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IntegerSearchCriteria implements SearchCriteria<Integer> {
    @Positive
    Integer value;
    String field;
    OperationType operation;
}
