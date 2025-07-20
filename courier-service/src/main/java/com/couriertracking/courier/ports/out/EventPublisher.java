package com.couriertracking.courier.ports.out;

import com.couriertracking.courier.domain.model.CourierLocationEvent;

public interface EventPublisher {
    void publishLocationEvent(CourierLocationEvent event);
}