package com.couriertracking.courier.adapters.out.mongodb;

import com.couriertracking.courier.domain.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends MongoRepository<Store, String> {
    Optional<Store> findByName(String name);

    List<Store> findAll();
}