package com.couriertracking.courier.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.couriertracking.courier.domain.model.CourierLocationData;
import com.couriertracking.courier.ports.in.CourierLocationUseCase;
import com.couriertracking.courier.ports.out.EventPublisher;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierLocationService implements CourierLocationUseCase {

    private final EventPublisher eventPublisher;

    @Override
    public void reportLocation(CourierLocationData locationData) {
        log.info("Processing location report for courier: {} at ({}, {})",
                locationData.getCourierId(),
                locationData.getLocation().getLatitude(),
                locationData.getLocation().getLongitude());

        try {
            eventPublisher.publishLocationEvent(locationData.toEvent());
            log.info("Successfully published location event for courier: {}", locationData.getCourierId());
        } catch (Exception e) {
            log.error("Failed to publish location event for courier: {}", locationData.getCourierId(), e);
            throw new RuntimeException("Failed to publish location event", e);
        }
    }
}