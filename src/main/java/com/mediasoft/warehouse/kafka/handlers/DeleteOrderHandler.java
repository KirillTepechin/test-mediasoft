package com.mediasoft.warehouse.kafka.handlers;

import com.mediasoft.warehouse.kafka.Event;
import com.mediasoft.warehouse.kafka.EventSource;
import com.mediasoft.warehouse.kafka.events.DeleteOrderEventData;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class DeleteOrderHandler implements EventHandler<DeleteOrderEventData>{
    private final OrderService orderService;

    @Override
    public boolean canHandle(EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.DELETE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(DeleteOrderEventData eventSource) {
        orderService.deleteOrder(eventSource.getOrderUuid(), eventSource.getCustomerId());
        return null;
    }
}
