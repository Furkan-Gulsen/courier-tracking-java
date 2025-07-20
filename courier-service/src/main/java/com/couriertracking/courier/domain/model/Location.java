package com.couriertracking.courier.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    private Double latitude;
    private Double longitude;

    public static Location of(Double lat, Double lng) {
        return new Location(lat, lng);
    }
}