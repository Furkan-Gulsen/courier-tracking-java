package com.couriertracking.tracking.adapters.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.couriertracking.tracking.domain.model.CourierLocationEvent;
import com.couriertracking.tracking.ports.in.CourierTrackingUseCase;

@Component
@RequiredArgsConstructor
@Slf4j
public class CourierLocationEventListener {

    private final CourierTrackingUseCase courierTrackingUseCase;

    @KafkaListener(topics = "courier-location-events", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload CourierLocationEvent event, Acknowledgment acknowledgment) {
        try {
            log.info("Received location event for courier: {} at ({}, {})",
                    event.getCourierId(),
                    event.getLocation().getLatitude(),
                    event.getLocation().getLongitude());

            courierTrackingUseCase.processLocationEvent(event);

            acknowledgment.acknowledge();
            log.debug("Successfully processed and acknowledged location event for courier: {}", event.getCourierId());
        } catch (Exception e) {
            log.error("Error processing location event for courier: {}", event.getCourierId(), e);
            acknowledgment.acknowledge();
        }
    }
}