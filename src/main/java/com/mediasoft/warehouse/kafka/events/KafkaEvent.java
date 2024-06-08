package com.mediasoft.warehouse.kafka.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.kafka.EventSource;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateOrderStatusEventData.class, name = "UPDATE_ORDER_STATUS"),
        @JsonSubTypes.Type(value = CreateOrderEventData.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = UpdateOrderEventData.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = DeleteOrderEventData.class, name = "DELETE_ORDER"),
})
public interface KafkaEvent extends EventSource {
}
