package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.dto.GetOrderProductDto;
import com.mediasoft.warehouse.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query("SELECT new com.mediasoft.warehouse.dto.GetOrderProductDto(p.uuid as productId, p.name, op.quantity, op.price) \n" +
            "FROM OrderProduct op JOIN Product p ON op.id.productId = p.uuid WHERE op.id.orderId = :orderId")
    List<GetOrderProductDto> findOrderProductsByOrderId(UUID orderId);

    @Override
    @EntityGraph(attributePaths = {"orderProducts", "customer"})
    Optional<Order> findById(UUID uuid);
}
