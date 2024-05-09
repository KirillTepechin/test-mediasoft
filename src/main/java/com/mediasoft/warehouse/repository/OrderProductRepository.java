package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.OrderProduct;
import com.mediasoft.warehouse.model.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {
    @Query("SELECT op FROM OrderProduct op WHERE op.id.orderUuid = :orderUuid")
    List<OrderProduct> findAllByOrderUuid(UUID orderUuid);
}
