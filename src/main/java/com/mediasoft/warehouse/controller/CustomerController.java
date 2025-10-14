package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.GetCustomerDto;
import com.mediasoft.warehouse.dto.GetProductDto;
import com.mediasoft.warehouse.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping
    public List<GetCustomerDto> getAllCustomers(Pageable pageable) {
        return customerService.getAlCustomers(pageable);
    }
}
