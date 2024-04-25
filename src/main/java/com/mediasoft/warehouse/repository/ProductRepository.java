package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

/**
 * Репозиторий товара.
 */
public interface ProductRepository extends JpaRepository<Product, UUID> , JpaSpecificationExecutor<Product> {
}
