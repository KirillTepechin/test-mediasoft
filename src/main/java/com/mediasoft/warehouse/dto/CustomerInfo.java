package com.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerInfo {
    private Long id;
    private String accountNumber;
    private String email;
    private String inn;
}
