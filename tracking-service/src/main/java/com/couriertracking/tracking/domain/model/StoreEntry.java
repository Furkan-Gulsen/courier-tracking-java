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
@Document(collection = "store_entries")
public class StoreEntry {
    @Id
    private String id;
    private String courierId;
    private String storeName;
    private Location storeLocation;
    private Location courierLocation;
    private Instant entryTime;

    public static StoreEntry of(String courierId, String storeName, Location storeLocation, Location courierLocation) {
        return new StoreEntry(null, courierId, storeName, storeLocation, courierLocation, Instant.now());
    }
}