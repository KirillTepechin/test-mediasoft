package com.mediasoft.warehouse.service.search;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum OperationType {
    @JsonAlias("=")
    EQUAL,
    @JsonAlias(">=")
    GREATER_THAN_OR_EQ,
    @JsonAlias("<=")
    LESS_THAN_OR_EQ,
    @JsonAlias("~")
    LIKE
}

