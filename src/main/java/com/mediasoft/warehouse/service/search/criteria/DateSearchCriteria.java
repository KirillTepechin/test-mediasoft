package com.mediasoft.warehouse.service.search.criteria;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mediasoft.warehouse.service.search.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DateSearchCriteria implements SearchCriteria<LocalDate> {
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    LocalDate value;
    String field;
    OperationType operation;
}
