package com.mediasoft.warehouse.kafka.handlers;

import com.mediasoft.warehouse.kafka.Event;
import com.mediasoft.warehouse.kafka.EventSource;
import com.mediasoft.warehouse.kafka.events.CreateOrderEventData;
import com.mediasoft.warehouse.mapper.OrderMapper;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateOrderHandler implements EventHandler<CreateOrderEventData> {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    public boolean canHandle(EventSource eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");

        return Event.CREATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(CreateOrderEventData eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        UUID uuid = orderService.createOrder(eventSource.getCustomerId(),
                orderMapper.toCreateOrderDto(eventSource));
        return uuid.toString();
    }
}
