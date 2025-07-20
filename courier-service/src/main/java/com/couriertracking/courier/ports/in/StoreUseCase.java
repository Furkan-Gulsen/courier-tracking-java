package com.couriertracking.courier.ports.in;

import java.util.List;
import java.util.Optional;

import com.couriertracking.courier.domain.model.Store;

public interface StoreUseCase {
    Store createStore(Store store);

    Optional<Store> getStoreById(String id);

    List<Store> getAllStores();

    Store updateStore(String id, Store store);

    void deleteStore(String id);
}