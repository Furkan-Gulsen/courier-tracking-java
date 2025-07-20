package com.couriertracking.tracking.adapters.out.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.couriertracking.tracking.domain.model.CourierTravel;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourierTravelRepository extends MongoRepository<CourierTravel, String> {
    Optional<CourierTravel> findFirstByCourierId(String courierId);

    List<CourierTravel> findByCourierIdOrderByTimestampAsc(String courierId);
}