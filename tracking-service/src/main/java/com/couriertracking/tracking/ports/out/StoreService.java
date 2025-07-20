package com.couriertracking.tracking.ports.out;

import com.couriertracking.tracking.domain.model.Location;
import com.couriertracking.tracking.domain.model.Store;

import java.util.List;

public interface StoreService {
    List<Store> findStoresWithinRadius(Location location, double radiusInMeters);

    List<Store> getAllStores();
}