package com.mediasoft.warehouse.kafka.events;

import com.mediasoft.warehouse.kafka.Event;
import lombok.Data;

import java.util.UUID;

@Data
public class DeleteOrderEventData implements KafkaEvent{
    private UUID orderUuid;
    private Long customerId;
    @Override
    public Event getEvent() {
        return Event.DELETE_ORDER;
    }
}
