package com.couriertracking.tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courier_travels")
public class CourierTravel {
    @Id
    private String id;
    private String courierId;
    private Location fromLocation;
    private Location toLocation;
    private Double distance;
    private Instant timestamp;

    public static CourierTravel of(String courierId, Location fromLocation, Location toLocation, Double distance) {
        return new CourierTravel(null, courierId, fromLocation, toLocation, distance, Instant.now());
    }
}