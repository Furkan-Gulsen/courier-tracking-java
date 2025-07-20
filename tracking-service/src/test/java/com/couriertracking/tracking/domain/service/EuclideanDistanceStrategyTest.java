package com.couriertracking.tracking.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.couriertracking.tracking.domain.model.Location;
import com.couriertracking.tracking.domain.service.EuclideanDistanceStrategy;

import static org.junit.jupiter.api.Assertions.*;

public class EuclideanDistanceStrategyTest {

    private EuclideanDistanceStrategy distanceStrategy;

    @BeforeEach
    void setUp() {
        distanceStrategy = new EuclideanDistanceStrategy();
    }

    @Test
    void shouldCalculateDistanceBetweenTwoPoints() {
        // Given
        Location point1 = new Location(40.0, 30.0);
        Location point2 = new Location(41.0, 31.0);

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        // Euclidean distance should be approximately sqrt(2) degrees * 111.32 km/degree
        double expectedDistance = Math.sqrt(2) * 111.32 * 1000; // Convert to meters
        assertEquals(expectedDistance, distance, 0.1);
    }

    @Test
    void shouldReturnZeroForSameLocation() {
        // Given
        Location location = new Location(41.0082, 28.9784);

        // When
        Double distance = distanceStrategy.calculateDistance(location, location);

        // Then
        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void shouldCalculateShortDistances() {
        // Given
        // Two points with small difference
        Location point1 = new Location(40.9923307, 29.1244229);
        Location point2 = new Location(40.9923307, 29.1254229); // 0.001 degree difference in longitude

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        // 0.001 degrees * 111.32 km/degree = ~111 meters
        assertEquals(111.32, distance, 0.1);
    }

    @Test
    void shouldHandleLatitudeDifference() {
        // Given
        Location point1 = new Location(40.0, 30.0);
        Location point2 = new Location(41.0, 30.0); // 1 degree latitude difference

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        // 1 degree * 111.32 km/degree = 111.32 km
        assertEquals(111320.0, distance, 0.1);
    }

    @Test
    void shouldHandleLongitudeDifference() {
        // Given
        Location point1 = new Location(40.0, 30.0);
        Location point2 = new Location(40.0, 31.0); // 1 degree longitude difference

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        // 1 degree * 111.32 km/degree = 111.32 km
        assertEquals(111320.0, distance, 0.1);
    }

    @Test
    void shouldHandleNegativeCoordinates() {
        // Given
        Location point1 = new Location(-40.0, -30.0);
        Location point2 = new Location(-41.0, -31.0);

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        // Euclidean distance should be approximately sqrt(2) degrees * 111.32 km/degree
        double expectedDistance = Math.sqrt(2) * 111.32 * 1000; // Convert to meters
        assertEquals(expectedDistance, distance, 0.1);
    }

    @Test
    void shouldHandleNullLocations() {
        // Given
        Location location = new Location(41.0082, 28.9784);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            distanceStrategy.calculateDistance(null, location);
        });

        assertThrows(NullPointerException.class, () -> {
            distanceStrategy.calculateDistance(location, null);
        });
    }
}