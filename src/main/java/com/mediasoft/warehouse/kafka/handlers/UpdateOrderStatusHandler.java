package com.mediasoft.warehouse.kafka.handlers;

import com.mediasoft.warehouse.dto.OrderStatusDto;
import com.mediasoft.warehouse.kafka.Event;
import com.mediasoft.warehouse.kafka.EventSource;
import com.mediasoft.warehouse.kafka.events.UpdateOrderStatusEventData;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class UpdateOrderStatusHandler implements EventHandler<UpdateOrderStatusEventData>{
    private final OrderService orderService;
    @Override
    public boolean canHandle(EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.UPDATE_ORDER_STATUS.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(UpdateOrderStatusEventData eventSource) {
        OrderStatus orderStatus = orderService.changeOrderStatus(eventSource.getOrderUuid(),
                new OrderStatusDto(eventSource.getStatus()));
        return orderStatus.name();
    }
}
