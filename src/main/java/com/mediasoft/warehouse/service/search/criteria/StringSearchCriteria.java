package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.service.search.OperationType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringSearchCriteria implements SearchCriteria<String> {
    @NotBlank(message = "Поле value не может быть пусто")
    String value;
    String field;
    OperationType operation;
}
