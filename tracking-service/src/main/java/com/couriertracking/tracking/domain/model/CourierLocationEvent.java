package com.couriertracking.tracking.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class CourierLocationEvent {

    private final String courierId;
    private final double latitude;
    private final double longitude;
    private final Instant timestamp;

    @JsonCreator
    public CourierLocationEvent(
            @JsonProperty("courierId") String courierId,
            @JsonProperty("latitude") double latitude,
            @JsonProperty("longitude") double longitude,
            @JsonProperty("timestamp") Instant timestamp) {

        this.courierId = Objects.requireNonNull(courierId, "Courier ID cannot be null");
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");

        validateCoordinates(latitude, longitude);
    }

    private void validateCoordinates(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }
}