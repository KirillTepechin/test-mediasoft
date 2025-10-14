package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий товара.
 */
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    @Query(value = "select * from product p where p.uuid = :uuid for update", nativeQuery = true)
    Optional<Product> findByIdLocked(UUID uuid);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    Product findByArticle(String article);
}
