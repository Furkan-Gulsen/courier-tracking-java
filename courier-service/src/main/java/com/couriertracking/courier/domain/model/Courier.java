package com.couriertracking.courier.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "couriers")
public class Courier {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private Location lastKnownLocation;
    private Instant lastLocationUpdate;
    private boolean active;

    public static Courier of(String name, String phoneNumber) {
        return new Courier(null, name, phoneNumber, null, null, true);
    }

    public static Courier of(String id, String name, String phoneNumber, boolean active) {
        return new Courier(id, name, phoneNumber, null, null, active);
    }

    public void updateLocation(Location location) {
        this.lastKnownLocation = location;
        this.lastLocationUpdate = Instant.now();
    }
}