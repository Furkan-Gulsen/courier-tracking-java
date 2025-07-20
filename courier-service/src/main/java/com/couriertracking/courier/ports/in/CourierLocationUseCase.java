package com.couriertracking.courier.ports.in;

import com.couriertracking.courier.domain.model.CourierLocationData;

public interface CourierLocationUseCase {
    void reportLocation(CourierLocationData locationData);
}