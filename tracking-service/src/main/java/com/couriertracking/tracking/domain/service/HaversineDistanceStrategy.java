package com.couriertracking.tracking.domain.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.couriertracking.tracking.domain.model.Location;

@Primary
@Component("haversineStrategy")
public class HaversineDistanceStrategy implements DistanceCalculationStrategy {

    private static final double EARTH_RADIUS_METERS = 6371000; // Earth's radius in meters

    @Override
    public Double calculateDistance(Location from, Location to) {
        double lat1Rad = Math.toRadians(from.getLatitude());
        double lat2Rad = Math.toRadians(to.getLatitude());
        double deltaLatRad = Math.toRadians(to.getLatitude() - from.getLatitude());
        double deltaLngRad = Math.toRadians(to.getLongitude() - from.getLongitude());

        double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLngRad / 2) * Math.sin(deltaLngRad / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }
}