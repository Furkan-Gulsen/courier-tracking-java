package com.couriertracking.courier.adapters.out;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.couriertracking.courier.domain.model.CourierLocationEvent;
import com.couriertracking.courier.ports.out.EventPublisher;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private static final String TOPIC_NAME = "courier-location-events";

    private final KafkaTemplate<String, CourierLocationEvent> kafkaTemplate;

    @Override
    public void publishLocationEvent(CourierLocationEvent event) {
        log.info("Publishing location event for courier: {} to topic: {}", event.getCourierId(), TOPIC_NAME);

        CompletableFuture<SendResult<String, CourierLocationEvent>> future = kafkaTemplate.send(TOPIC_NAME,
                event.getCourierId(), event);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to publish event to Kafka", exception);
                throw new RuntimeException("Failed to publish event to Kafka", exception);
            } else {
                log.info("Successfully published location event for courier: {}", event.getCourierId());
            }
        });
    }
}