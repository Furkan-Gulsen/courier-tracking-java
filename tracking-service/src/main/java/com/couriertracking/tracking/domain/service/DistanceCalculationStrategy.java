package com.couriertracking.tracking.domain.service;

import com.couriertracking.tracking.domain.model.Location;

public interface DistanceCalculationStrategy {
    /**
     * Calculate distance between two locations in meters
     * 
     * @param from Starting location
     * @param to   Ending location
     * @return Distance in meters
     */
    Double calculateDistance(Location from, Location to);
}