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

import com.couriertracking.courier.adapters.in.dto.StoreRequest;
import com.couriertracking.courier.adapters.in.dto.StoreResponse;
import com.couriertracking.courier.domain.model.Store;
import com.couriertracking.courier.ports.in.StoreUseCase;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Store Management", description = "APIs for managing store data")
public class StoreController {

    private final StoreUseCase storeUseCase;

    @PostMapping
    @Operation(summary = "Create store", description = "Create a new store with location")
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody StoreRequest request) {
        Store store = Store.of(request.getName(), request.getLat(), request.getLng());
        Store createdStore = storeUseCase.createStore(store);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(createdStore));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store", description = "Get store by ID")
    public ResponseEntity<StoreResponse> getStore(
            @Parameter(description = "Store ID") @PathVariable String id) {
        return storeUseCase.getStoreById(id)
                .map(store -> ResponseEntity.ok(mapToResponse(store)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Get all stores", description = "Get all stores")
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeUseCase.getAllStores().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stores);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update store", description = "Update an existing store")
    public ResponseEntity<StoreResponse> updateStore(
            @Parameter(description = "Store ID") @PathVariable String id,
            @Valid @RequestBody StoreRequest request) {
        try {
            Store store = Store.of(request.getName(), request.getLat(), request.getLng());
            Store updatedStore = storeUseCase.updateStore(id, store);
            return ResponseEntity.ok(mapToResponse(updatedStore));
        } catch (IllegalArgumentException e) {
            log.error("Failed to update store: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete store", description = "Delete a store by ID")
    public ResponseEntity<Void> deleteStore(
            @Parameter(description = "Store ID") @PathVariable String id) {
        try {
            storeUseCase.deleteStore(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Failed to delete store: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    private StoreResponse mapToResponse(Store store) {
        return new StoreResponse(
                store.getId(),
                store.getName(),
                store.getLocation().getLatitude(),
                store.getLocation().getLongitude());
    }
}