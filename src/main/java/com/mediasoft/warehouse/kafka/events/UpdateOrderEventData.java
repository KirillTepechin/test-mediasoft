package com.mediasoft.warehouse.kafka.events;

import com.mediasoft.warehouse.dto.OrderProductDto;
import com.mediasoft.warehouse.kafka.Event;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateOrderEventData implements KafkaEvent{
    private UUID orderUuid;
    private Long customerId;
    private List<OrderProductDto> products;

    @Override
    public Event getEvent() {
        return Event.UPDATE_ORDER;
    }
}
