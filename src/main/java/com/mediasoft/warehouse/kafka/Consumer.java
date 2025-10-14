package com.mediasoft.warehouse.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediasoft.warehouse.kafka.events.KafkaEvent;
import com.mediasoft.warehouse.kafka.handlers.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
public class Consumer {

    private final Set<EventHandler<EventSource>> eventHandlers;

    @KafkaListener(topics = "test_topic", containerFactory = "kafkaListenerContainerFactoryString")
    public void listenGroupTopic2(String message) {
        log.info("Receive message: {}", message);
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);
            log.info("EventSource: {}", eventSource);
            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler for eventsource not found"))
                    .handleEvent(eventSource);

        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message: {}; exception: ", message, e);
        }
    }
}