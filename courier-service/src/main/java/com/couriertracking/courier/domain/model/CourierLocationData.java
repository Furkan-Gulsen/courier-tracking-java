package com.couriertracking.courier.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationData {
    private String courierId;
    private Location location;
    private Instant timestamp;

    public static CourierLocationData of(String courierId, Double lat, Double lng, Instant timestamp) {
        return new CourierLocationData(courierId, Location.of(lat, lng), timestamp);
    }

    public CourierLocationEvent toEvent() {
        return new CourierLocationEvent(
                courierId,
                timestamp,
                location.getLatitude(),
                location.getLongitude());
    }
}