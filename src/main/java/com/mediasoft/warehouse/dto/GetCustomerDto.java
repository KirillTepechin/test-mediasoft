package com.mediasoft.warehouse.dto;

import lombok.Data;

@Data
public class GetCustomerDto {

    private Long id;

    private String login;

    private String email;

    private Boolean isActive;
}
