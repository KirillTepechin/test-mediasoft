package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
