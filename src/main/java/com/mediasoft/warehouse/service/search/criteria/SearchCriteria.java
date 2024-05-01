package com.mediasoft.warehouse.service.search.criteria;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.service.search.OperationType;
import jakarta.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UUIDSearchCriteria.class, name = "uuid"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = StringSearchCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = BigDecimalSearchCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = DateSearchCriteria.class, name = "createdDate"),
        @JsonSubTypes.Type(value = DateSearchCriteria.class, name = "lastQuantityChangeDate"),
        @JsonSubTypes.Type(value = IntegerSearchCriteria.class, name = "quantity"),
        @JsonSubTypes.Type(value = CategorySearchCriteria.class, name = "category"),
})
public interface SearchCriteria<T> {
    @NotNull(message = "Поле value не должно быть null")
    T getValue();
    String getField();
    @NotNull(message = "Поле operation не должно быть null")
    OperationType getOperation();
}

