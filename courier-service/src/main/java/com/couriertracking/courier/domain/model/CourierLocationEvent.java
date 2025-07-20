package com.couriertracking.courier.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourierLocationEvent {
    private String courierId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;

    private Double lat;
    private Double lng;
}