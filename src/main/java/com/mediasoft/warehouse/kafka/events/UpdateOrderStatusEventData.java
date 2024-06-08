package com.mediasoft.warehouse.kafka.events;

import com.mediasoft.warehouse.kafka.Event;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateOrderStatusEventData implements KafkaEvent {
    private UUID orderUuid;
    private OrderStatus status;
    @Override
    public Event getEvent() {
        return Event.UPDATE_ORDER_STATUS;
    }
}
