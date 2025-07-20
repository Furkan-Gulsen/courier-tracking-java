package com.couriertracking.courier.adapters.in.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Courier location data")
public class CourierLocationRequest {

    @NotNull
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    @Schema(description = "Latitude coordinate", example = "40.9923307", minimum = "-90", maximum = "90")
    private Double lat;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    @Schema(description = "Longitude coordinate", example = "29.1244229", minimum = "-180", maximum = "180")
    private Double lng;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(description = "Timestamp of the location (optional, defaults to current time)", example = "2024-01-01T12:00:00Z")
    private Instant time;
}