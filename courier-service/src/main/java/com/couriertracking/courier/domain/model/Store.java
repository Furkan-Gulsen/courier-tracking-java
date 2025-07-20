package com.couriertracking.courier.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stores")
public class Store {
    @Id
    private String id;
    private String name;
    private Location location;

    public static Store of(String name, Double lat, Double lng) {
        return new Store(null, name, Location.of(lat, lng));
    }

    public static Store of(String id, String name, Double lat, Double lng) {
        return new Store(id, name, Location.of(lat, lng));
    }
}