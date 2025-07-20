package com.couriertracking.courier.adapters.out.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.couriertracking.courier.domain.model.Courier;

@Repository
public interface CourierRepository extends MongoRepository<Courier, String> {
}