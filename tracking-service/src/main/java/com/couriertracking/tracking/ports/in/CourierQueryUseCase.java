package com.couriertracking.tracking.ports.in;

import com.couriertracking.tracking.domain.model.StoreEntry;

import java.util.List;

public interface CourierQueryUseCase {
    Double getTotalTravelDistance(String courierId);

    List<StoreEntry> getStoreEntries(String courierId);
}