package com.couriertracking.tracking.ports.in;

import com.couriertracking.tracking.domain.model.CourierLocationEvent;

public interface CourierTrackingUseCase {
    void processLocationEvent(CourierLocationEvent event);
}