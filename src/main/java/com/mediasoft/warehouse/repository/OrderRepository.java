package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.dto.GetOrderDto;
import com.mediasoft.warehouse.dto.GetOrderProductDto;
import com.mediasoft.warehouse.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {


    @Query("SELECT new com.mediasoft.warehouse.dto.GetOrderProductDto(p.uuid as productUuid, p.name, op.quantity, op.price) \n" +
            "FROM OrderProduct op JOIN Product p ON op.id.productUuid = p.uuid WHERE op.id.orderUuid = :orderUuid")
    List<GetOrderProductDto> findOrderProductsByOrderUuid(UUID orderUuid);
}
