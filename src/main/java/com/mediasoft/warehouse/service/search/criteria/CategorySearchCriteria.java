package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.model.Category;
import com.mediasoft.warehouse.service.search.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategorySearchCriteria implements SearchCriteria<Category> {
    Category value;
    String field;
    OperationType operation;
}
