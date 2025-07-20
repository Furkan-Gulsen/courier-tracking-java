package com.couriertracking.tracking.adapters.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.couriertracking.tracking.domain.model.StoreEntry;
import com.couriertracking.tracking.ports.in.CourierQueryUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courier Query", description = "APIs for querying courier tracking data")
public class CourierQueryController {

    private final CourierQueryUseCase courierQueryUseCase;

    @GetMapping(value = { "/{courierId}/total-travel-distance", "/total-travel-distance" })
    @Operation(summary = "Get total travel distance", description = "Get the total distance traveled by a courier in meters")
    public ResponseEntity<Double> getTotalTravelDistance(
            @Parameter(description = "Courier ID", example = "courier123") @PathVariable(required = false) String courierId) {
        if (courierId == null || courierId.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }

        Double totalDistance = courierQueryUseCase.getTotalTravelDistance(courierId);

        return ResponseEntity.ok(totalDistance);
    }

    @GetMapping("/{courierId}/store-entries")
    @Operation(summary = "Get store entries", description = "Get all store entries for a courier")
    public ResponseEntity<List<StoreEntry>> getStoreEntries(
            @Parameter(description = "Courier ID", example = "courier123") @PathVariable String courierId) {
        List<StoreEntry> entries = courierQueryUseCase.getStoreEntries(courierId);

        if (entries.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(entries);
    }
}