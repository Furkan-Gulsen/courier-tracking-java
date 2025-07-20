package com.couriertracking.courier.adapters.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.couriertracking.courier.adapters.in.dto.CourierLocationRequest;
import com.couriertracking.courier.domain.model.CourierLocationData;
import com.couriertracking.courier.ports.in.CourierLocationUseCase;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courier Tracking", description = "APIs for courier location tracking")
public class CourierLocationController {

        private final CourierLocationUseCase courierLocationUseCase;

        @PostMapping("/{courierId}/location")
        @Operation(summary = "Report courier location", description = "Submit a new location for a courier")
        public ResponseEntity<Void> reportLocation(
                        @Parameter(description = "Courier ID", example = "courier123") @PathVariable String courierId,
                        @Valid @RequestBody CourierLocationRequest request) {

                CourierLocationData locationData = CourierLocationData.of(
                                courierId,
                                request.getLat(),
                                request.getLng(),
                                request.getTime() != null ? request.getTime() : Instant.now());

                courierLocationUseCase.reportLocation(locationData);

                return ResponseEntity.ok().build();
        }
}