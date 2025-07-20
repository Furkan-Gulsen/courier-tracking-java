package com.couriertracking.courier.application;

import com.couriertracking.courier.domain.model.Store;
import com.couriertracking.courier.ports.in.StoreUseCase;
import com.couriertracking.courier.adapters.out.mongodb.StoreRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService implements StoreUseCase {

    private final StoreRepository storeRepository;

    @Override
    @Caching(put = { @CachePut(value = "stores", key = "#result.id", condition = "#result != null") }, evict = {
            @CacheEvict(value = "allStores", allEntries = true) })
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    @Cacheable(value = "stores", key = "#id")
    public Optional<Store> getStoreById(String id) {
        return storeRepository.findById(id);
    }

    @Override
    @Cacheable(value = "allStores")
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    @Caching(put = { @CachePut(value = "stores", key = "#id", condition = "#result != null") }, evict = {
            @CacheEvict(value = "allStores", allEntries = true) })
    public Store updateStore(String id, Store store) {
        if (!storeRepository.existsById(id)) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }

        store.setId(id);
        return storeRepository.save(store);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "stores", key = "#id"),
            @CacheEvict(value = "allStores", allEntries = true)
    })
    public void deleteStore(String id) {
        if (!storeRepository.existsById(id)) {
            throw new IllegalArgumentException("Store not found with ID: " + id);
        }

        storeRepository.deleteById(id);
    }
}