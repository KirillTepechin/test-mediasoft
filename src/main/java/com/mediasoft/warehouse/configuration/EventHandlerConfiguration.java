package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.kafka.EventSource;
import com.mediasoft.warehouse.kafka.handlers.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class EventHandlerConfiguration {
    @Bean
    <T extends EventSource> Set<EventHandler<T>> eventHandlers(Set<EventHandler<T>> eventHandlers) {
        return new HashSet<>(eventHandlers);
    }
}
