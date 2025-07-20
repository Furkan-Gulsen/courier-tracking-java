package com.couriertracking.tracking.application;

import com.couriertracking.tracking.domain.model.Location;
import com.couriertracking.tracking.domain.model.Store;
import com.couriertracking.tracking.domain.service.DistanceCalculationStrategy;
import com.couriertracking.tracking.ports.out.StoreService;
import com.couriertracking.tracking.adapters.out.mongodb.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Qualifier("haversineStrategy")
    private final DistanceCalculationStrategy distanceCalculationStrategy;

    @Override
    @Cacheable(value = "nearbyStores", key = "{#location.latitude, #location.longitude, #radiusInMeters}")
    public List<Store> findStoresWithinRadius(Location location, double radiusInMeters) {
        List<Store> allStores = getAllStores();

        return allStores.stream()
                .filter(store -> {
                    double distance = distanceCalculationStrategy.calculateDistance(
                            location, store.getLocation());
                    return distance <= radiusInMeters;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "allStores")
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }
}