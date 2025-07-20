package com.couriertracking.courier.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.couriertracking.courier.adapters.out.mongodb.CourierRepository;
import com.couriertracking.courier.domain.model.Courier;
import com.couriertracking.courier.ports.in.CourierUseCase;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService implements CourierUseCase {

    private final CourierRepository courierRepository;

    @Override
    public Courier createCourier(Courier courier) {
        return courierRepository.save(courier);
    }

    @Override
    public Optional<Courier> getCourierById(String id) {
        return courierRepository.findById(id);
    }

    @Override
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    @Override
    public Courier updateCourier(String id, Courier courier) {
        if (!courierRepository.existsById(id)) {
            throw new IllegalArgumentException("Courier not found with ID: " + id);
        }

        courier.setId(id);
        return courierRepository.save(courier);
    }

    @Override
    public void deleteCourier(String id) {
        if (!courierRepository.existsById(id)) {
            throw new IllegalArgumentException("Courier not found with ID: " + id);
        }

        courierRepository.deleteById(id);
    }

    @Override
    public void activateCourier(String id) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found with ID: " + id));

        courier.setActive(true);
        courierRepository.save(courier);
    }

    @Override
    public void deactivateCourier(String id) {
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found with ID: " + id));

        courier.setActive(false);
        courierRepository.save(courier);
    }
}