package com.couriertracking.tracking.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.couriertracking.tracking.adapters.out.mongodb.CourierTravelRepository;
import com.couriertracking.tracking.adapters.out.mongodb.StoreEntryRepository;
import com.couriertracking.tracking.domain.model.*;
import com.couriertracking.tracking.domain.service.DistanceCalculationStrategy;
import com.couriertracking.tracking.ports.in.CourierTrackingUseCase;
import com.couriertracking.tracking.ports.out.StoreService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierTrackingService implements CourierTrackingUseCase {

    private static final double STORE_RADIUS_METERS = 100.0;
    private static final int MIN_MINUTES_BETWEEN_ENTRIES = 1;

    private final CourierTravelRepository courierTravelRepository;
    private final StoreEntryRepository storeEntryRepository;

    private final StoreService storeService;

    @Qualifier("haversineStrategy")
    private final DistanceCalculationStrategy distanceCalculationStrategy;

    @Override
    public void processLocationEvent(CourierLocationEvent event) {
        Location currentLocation = event.getLocation();
        processTravelDistance(event.getCourierId(), currentLocation);
        processStoreProximity(event.getCourierId(), currentLocation);
    }

    private void processTravelDistance(String courierId, Location currentLocation) {
        Optional<CourierTravel> lastTravelOpt = courierTravelRepository.findFirstByCourierId(courierId);

        if (lastTravelOpt.isPresent()) {
            CourierTravel lastTravel = lastTravelOpt.get();
            Location lastLocation = lastTravel.getToLocation();

            Double distance = distanceCalculationStrategy.calculateDistance(lastLocation, currentLocation);

            if (distance > 0.1) {
                CourierTravel newTravel = CourierTravel.of(courierId, lastLocation, currentLocation, distance);
                courierTravelRepository.save(newTravel);

                log.debug("Recorded travel for courier: {} - distance: {} meters", courierId, distance);
            } else {
                log.debug("Ignoring minimal movement for courier: {} - distance: {} meters", courierId, distance);
            }
        } else {
            CourierTravel initialTravel = CourierTravel.of(courierId, currentLocation, currentLocation, 0.0);
            courierTravelRepository.save(initialTravel);
            log.debug("Recorded initial location for courier: {}", courierId);
        }
    }

    private void processStoreProximity(String courierId, Location currentLocation) {
        List<Store> nearbyStores = storeService.findStoresWithinRadius(currentLocation, STORE_RADIUS_METERS);

        for (Store store : nearbyStores) {
            processStoreEntry(courierId, store, currentLocation);
        }
    }

    private void processStoreEntry(String courierId, Store store, Location courierLocation) {
        Instant oneMinuteAgo = Instant.now().minus(MIN_MINUTES_BETWEEN_ENTRIES, ChronoUnit.MINUTES);

        Optional<StoreEntry> recentEntryOpt = storeEntryRepository
                .findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(courierId, store.getName(), oneMinuteAgo);

        if (recentEntryOpt.isEmpty()) {
            StoreEntry entry = StoreEntry.of(courierId, store.getName(), store.getLocation(), courierLocation);
            storeEntryRepository.save(entry);

        } else {
            log.debug("Courier: {} already entered store: {} within last minute, ignoring re-entry",
                    courierId, store.getName());
        }
    }
}