package com.couriertracking.courier.adapters.in.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private Double lastLat;
    private Double lastLng;
    private Instant lastLocationUpdate;
    private boolean active;
}