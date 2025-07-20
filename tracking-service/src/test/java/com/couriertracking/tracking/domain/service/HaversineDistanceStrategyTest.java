package com.couriertracking.tracking.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.couriertracking.tracking.domain.model.Location;
import com.couriertracking.tracking.domain.service.HaversineDistanceStrategy;

import static org.junit.jupiter.api.Assertions.*;

public class HaversineDistanceStrategyTest {

    private HaversineDistanceStrategy distanceStrategy;

    @BeforeEach
    void setUp() {
        distanceStrategy = new HaversineDistanceStrategy();
    }

    @Test
    void shouldCalculateDistanceBetweenTwoPoints() {
        // Given
        Location istanbul = new Location(41.0082, 28.9784); // Istanbul
        Location ankara = new Location(39.9334, 32.8597); // Ankara

        // When
        Double distance = distanceStrategy.calculateDistance(istanbul, ankara);

        // Then
        // (350,000-400,000 meters)
        assertTrue(distance > 300000 && distance < 450000);
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
        Location point1 = new Location(40.9923307, 29.1244229);
        Location point2 = new Location(40.9923307, 29.1255000);

        // When
        Double distance = distanceStrategy.calculateDistance(point1, point2);

        // Then
        assertTrue(distance > 80 && distance < 120);
    }

    @Test
    void shouldHandleEquatorCrossing() {
        // Given
        Location northOfEquator = new Location(1.0, 30.0);
        Location southOfEquator = new Location(-1.0, 30.0);

        // When
        Double distance = distanceStrategy.calculateDistance(northOfEquator, southOfEquator);

        // Then
        assertTrue(distance > 210000 && distance < 230000);
    }

    @Test
    void shouldHandleInternationalDateLineCrossing() {
        // Given
        Location westOfDateLine = new Location(0.0, 179.0);
        Location eastOfDateLine = new Location(0.0, -179.0);

        // When
        Double distance = distanceStrategy.calculateDistance(westOfDateLine, eastOfDateLine);

        // Then
        assertTrue(distance > 210000 && distance < 230000);
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