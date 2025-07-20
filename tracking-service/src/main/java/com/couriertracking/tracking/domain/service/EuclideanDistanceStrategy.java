package com.couriertracking.tracking.domain.service;

import org.springframework.stereotype.Component;

import com.couriertracking.tracking.domain.model.Location;

@Component("euclideanStrategy")
public class EuclideanDistanceStrategy implements DistanceCalculationStrategy {

    private static final double METERS_PER_DEGREE = 111320.0; // Approximate meters per degree (111.32 km)

    @Override
    public Double calculateDistance(Location from, Location to) {
        double deltaLat = to.getLatitude() - from.getLatitude();
        double deltaLng = to.getLongitude() - from.getLongitude();

        double distance = Math.sqrt(deltaLat * deltaLat + deltaLng * deltaLng);

        return distance * METERS_PER_DEGREE;
    }
}