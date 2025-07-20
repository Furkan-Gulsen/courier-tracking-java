package com.couriertracking.tracking.adapters.out.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.couriertracking.tracking.domain.model.StoreEntry;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreEntryRepository extends MongoRepository<StoreEntry, String> {
    Optional<StoreEntry> findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(
            String courierId, String storeName, Instant entryTime);

    List<StoreEntry> findByCourierIdOrderByEntryTimeDesc(String courierId);
}