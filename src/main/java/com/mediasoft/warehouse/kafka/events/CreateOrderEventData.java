package com.mediasoft.warehouse.kafka.events;

import com.mediasoft.warehouse.dto.OrderProductDto;
import com.mediasoft.warehouse.kafka.Event;
import lombok.Data;

import java.util.List;
@Data
public class CreateOrderEventData implements KafkaEvent{
    private Long customerId;
    private String deliveryAddress;
    private List<OrderProductDto> products;
    @Override
    public Event getEvent() {
        return Event.CREATE_ORDER;
    }
}
