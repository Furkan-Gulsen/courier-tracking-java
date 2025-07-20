package com.couriertracking.courier.ports.in;

import java.util.List;
import java.util.Optional;

import com.couriertracking.courier.domain.model.Courier;

public interface CourierUseCase {
    Courier createCourier(Courier courier);

    Optional<Courier> getCourierById(String id);

    List<Courier> getAllCouriers();

    Courier updateCourier(String id, Courier courier);

    void deleteCourier(String id);

    void activateCourier(String id);

    void deactivateCourier(String id);
}