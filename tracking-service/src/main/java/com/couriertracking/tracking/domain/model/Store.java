package com.couriertracking.tracking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Store {
    private String name;
    private Location location;

    public static Store of(String name, Double lat, Double lng) {
        return new Store(name, Location.of(lat, lng));
    }
}