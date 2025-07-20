package com.couriertracking.courier.adapters.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.couriertracking.courier.adapters.in.dto.CourierRequest;
import com.couriertracking.courier.adapters.in.dto.CourierResponse;
import com.couriertracking.courier.domain.model.Courier;
import com.couriertracking.courier.ports.in.CourierUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/couriers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Courier Management", description = "APIs for managing courier data")
public class CourierController {

    private final CourierUseCase courierUseCase;

    @PostMapping
    @Operation(summary = "Create courier", description = "Create a new courier")
    public ResponseEntity<CourierResponse> createCourier(@Valid @RequestBody CourierRequest request) {
        Courier courier = Courier.of(request.getName(), request.getPhoneNumber());
        Courier createdCourier = courierUseCase.createCourier(courier);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(createdCourier));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get courier", description = "Get courier by ID")
    public ResponseEntity<CourierResponse> getCourier(
            @Parameter(description = "Courier ID") @PathVariable String id) {
        return courierUseCase.getCourierById(id)
                .map(courier -> ResponseEntity.ok(mapToResponse(courier)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all couriers", description = "Get all couriers")
    public ResponseEntity<List<CourierResponse>> getAllCouriers() {
        List<CourierResponse> couriers = courierUseCase.getAllCouriers().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(couriers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update courier", description = "Update an existing courier")
    public ResponseEntity<CourierResponse> updateCourier(
            @Parameter(description = "Courier ID") @PathVariable String id,
            @Valid @RequestBody CourierRequest request) {
        try {
            Courier courier = courierUseCase.getCourierById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Courier not found with ID: " + id));

            courier.setName(request.getName());
            courier.setPhoneNumber(request.getPhoneNumber());

            Courier updatedCourier = courierUseCase.updateCourier(id, courier);
            return ResponseEntity.ok(mapToResponse(updatedCourier));
        } catch (IllegalArgumentException e) {
            log.error("Failed to update courier: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete courier", description = "Delete a courier by ID")
    public ResponseEntity<Void> deleteCourier(
            @Parameter(description = "Courier ID") @PathVariable String id) {
        try {
            courierUseCase.deleteCourier(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete courier: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate courier", description = "Activate a courier")
    public ResponseEntity<Void> activateCourier(
            @Parameter(description = "Courier ID") @PathVariable String id) {
        try {
            courierUseCase.activateCourier(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to activate courier: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate courier", description = "Deactivate a courier")
    public ResponseEntity<Void> deactivateCourier(
            @Parameter(description = "Courier ID") @PathVariable String id) {
        try {
            courierUseCase.deactivateCourier(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to deactivate courier: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private CourierResponse mapToResponse(Courier courier) {
        Double lastLat = null;
        Double lastLng = null;

        if (courier.getLastKnownLocation() != null) {
            lastLat = courier.getLastKnownLocation().getLatitude();
            lastLng = courier.getLastKnownLocation().getLongitude();
        }

        return new CourierResponse(
                courier.getId(),
                courier.getName(),
                courier.getPhoneNumber(),
                lastLat,
                lastLng,
                courier.getLastLocationUpdate(),
                courier.isActive());
    }
}