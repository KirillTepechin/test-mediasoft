package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.GetCustomerDto;
import com.mediasoft.warehouse.mapper.CustomerMapper;
import com.mediasoft.warehouse.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Transactional(readOnly = true)
    public List<GetCustomerDto> getAlCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).getContent().stream()
                .map(customerMapper::toGetCustomerDto).collect(Collectors.toList());
    }
}
