package com.mediasoft.warehouse.service.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "field", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SearchCriteria.UUIDSearchCriteria.class, name = "uuid"),
        @JsonSubTypes.Type(value = SearchCriteria.StringSearchCriteria.class, name = "name"),
        @JsonSubTypes.Type(value = SearchCriteria.StringSearchCriteria.class, name = "description"),
        @JsonSubTypes.Type(value = SearchCriteria.BigDecimalSearchCriteria.class, name = "price"),
        @JsonSubTypes.Type(value = SearchCriteria.DateSearchCriteria.class, name = "createdDate"),
        @JsonSubTypes.Type(value = SearchCriteria.DateSearchCriteria.class, name = "lastQuantityChangeDate"),
        @JsonSubTypes.Type(value = SearchCriteria.IntegerSearchCriteria.class, name = "quantity"),
        @JsonSubTypes.Type(value = SearchCriteria.CategorySearchCriteria.class, name = "category"),
})
public interface SearchCriteria<T> {
    @NotNull(message = "Поле value не должно быть null")
    T getValue();
    String getField();
    @NotNull(message = "Поле operation не должно быть null")
    OperationType getOperation();

    @Getter
    @AllArgsConstructor
    class StringSearchCriteria implements SearchCriteria<String> {
        @NotBlank(message = "Поле value не может быть пусто")
        String value;
        String field;
        OperationType operation;
    }
    @Getter
    @AllArgsConstructor
    class DateSearchCriteria implements SearchCriteria<LocalDate> {
        @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
        LocalDate value;
        String field;
        OperationType operation;
    }
    @Getter
    @AllArgsConstructor
    class BigDecimalSearchCriteria implements SearchCriteria<BigDecimal> {
        @Positive
        BigDecimal value;
        String field;
        OperationType operation;
    }
    @Getter
    @AllArgsConstructor
    class IntegerSearchCriteria implements SearchCriteria<Integer> {
        @Positive
        Integer value;
        String field;
        OperationType operation;
    }
    @Getter
    @AllArgsConstructor
    class CategorySearchCriteria implements SearchCriteria<Category> {
        Category value;
        String field;
        OperationType operation;
    }
    @Getter
    @AllArgsConstructor
    class UUIDSearchCriteria implements SearchCriteria<UUID> {
        UUID value;
        String field;
        OperationType operation;
    }
}

