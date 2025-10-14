package com.mediasoft.warehouse.kafka.handlers;

import com.mediasoft.warehouse.kafka.EventSource;

public interface EventHandler <T extends EventSource> {

    boolean canHandle(EventSource eventSource);

    String handleEvent(T eventSource);
}
