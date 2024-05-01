package com.mediasoft.warehouse.service.search.criteria;

import com.mediasoft.warehouse.service.search.OperationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UUIDSearchCriteria implements SearchCriteria<UUID> {
    UUID value;
    String field;
    OperationType operation;
}
