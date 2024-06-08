package com.mediasoft.warehouse.kafka.handlers;

import com.mediasoft.warehouse.kafka.Event;
import com.mediasoft.warehouse.kafka.EventSource;
import com.mediasoft.warehouse.kafka.events.UpdateOrderEventData;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateOrderHandler implements EventHandler<UpdateOrderEventData>{
    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.UPDATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(UpdateOrderEventData eventSource) {
        UUID uuid = orderService.addProductsToOrder(eventSource.getProducts(), eventSource.getOrderUuid(),
                eventSource.getCustomerId());
        return uuid.toString();
    }
}
