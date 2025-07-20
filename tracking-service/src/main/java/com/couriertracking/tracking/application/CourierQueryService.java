package com.couriertracking.tracking.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.couriertracking.tracking.adapters.out.mongodb.CourierTravelRepository;
import com.couriertracking.tracking.adapters.out.mongodb.StoreEntryRepository;
import com.couriertracking.tracking.domain.model.StoreEntry;
import com.couriertracking.tracking.ports.in.CourierQueryUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierQueryService implements CourierQueryUseCase {

    private final CourierTravelRepository courierTravelRepository;
    private final StoreEntryRepository storeEntryRepository;

    @Override
    public Double getTotalTravelDistance(String courierId) {
        if (courierId == null || courierId.isEmpty()) {
            log.warn("Invalid courier ID provided");
            return 0.0;
        }

        Double totalDistance = courierTravelRepository.findByCourierIdOrderByTimestampAsc(courierId)
                .stream()
                .mapToDouble(travel -> travel.getDistance() != null ? travel.getDistance() : 0.0)
                .sum();

        return totalDistance;
    }

    @Override
    public List<StoreEntry> getStoreEntries(String courierId) {
        return storeEntryRepository.findByCourierIdOrderByEntryTimeDesc(courierId);
    }
}