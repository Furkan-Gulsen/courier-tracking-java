package com.couriertracking.tracking.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couriertracking.tracking.adapters.out.mongodb.CourierTravelRepository;
import com.couriertracking.tracking.domain.model.CourierTravel;
import com.couriertracking.tracking.domain.model.Location;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierQueryServiceTest {

    @Mock
    private CourierTravelRepository courierTravelRepository;

    @InjectMocks
    private CourierQueryService courierQueryService;

    private final String COURIER_ID = "courier123";
    private List<CourierTravel> travelRecords;

    @BeforeEach
    void setUp() {
        Location loc1 = new Location(40.9923307, 29.1244229);
        Location loc2 = new Location(40.9925000, 29.1250000);
        Location loc3 = new Location(40.9930000, 29.1260000);

        CourierTravel travel1 = new CourierTravel();
        travel1.setId("travel1");
        travel1.setCourierId(COURIER_ID);
        travel1.setFromLocation(loc1);
        travel1.setToLocation(loc2);
        travel1.setDistance(100.0);
        travel1.setTimestamp(Instant.now().minusSeconds(300));

        CourierTravel travel2 = new CourierTravel();
        travel2.setId("travel2");
        travel2.setCourierId(COURIER_ID);
        travel2.setFromLocation(loc2);
        travel2.setToLocation(loc3);
        travel2.setDistance(150.0);
        travel2.setTimestamp(Instant.now().minusSeconds(200));

        travelRecords = Arrays.asList(travel1, travel2);
    }

    @Test
    void shouldGetTotalTravelDistance() {
        // Given
        when(courierTravelRepository.findByCourierIdOrderByTimestampAsc(anyString())).thenReturn(travelRecords);

        // When
        Double totalDistance = courierQueryService.getTotalTravelDistance(COURIER_ID);

        // Then
        assertEquals(250.0, totalDistance);
        verify(courierTravelRepository).findByCourierIdOrderByTimestampAsc(COURIER_ID);
    }

    @Test
    void shouldReturnZeroWhenNoTravelRecordsExist() {
        // Given
        when(courierTravelRepository.findByCourierIdOrderByTimestampAsc(anyString()))
                .thenReturn(Collections.emptyList());

        // When
        Double totalDistance = courierQueryService.getTotalTravelDistance(COURIER_ID);

        // Then
        assertEquals(0.0, totalDistance);
        verify(courierTravelRepository).findByCourierIdOrderByTimestampAsc(COURIER_ID);
    }

    @Test
    void shouldReturnZeroForNullCourierId() {
        // When
        Double totalDistance = courierQueryService.getTotalTravelDistance(null);

        // Then
        assertEquals(0.0, totalDistance);
        verify(courierTravelRepository, never()).findByCourierIdOrderByTimestampAsc(anyString());
    }

    @Test
    void shouldReturnZeroForEmptyCourierId() {
        // When
        Double totalDistance = courierQueryService.getTotalTravelDistance("");

        // Then
        assertEquals(0.0, totalDistance);
        verify(courierTravelRepository, never()).findByCourierIdOrderByTimestampAsc(anyString());
    }

    @Test
    void shouldHandleNullDistanceValues() {
        // Given
        CourierTravel travelWithNullDistance = new CourierTravel();
        travelWithNullDistance.setId("travel3");
        travelWithNullDistance.setCourierId(COURIER_ID);
        travelWithNullDistance.setDistance(null);

        List<CourierTravel> mixedRecords = Arrays.asList(travelRecords.get(0), travelWithNullDistance);
        when(courierTravelRepository.findByCourierIdOrderByTimestampAsc(anyString())).thenReturn(mixedRecords);

        // When
        Double totalDistance = courierQueryService.getTotalTravelDistance(COURIER_ID);

        // Then
        assertEquals(100.0, totalDistance);
        verify(courierTravelRepository).findByCourierIdOrderByTimestampAsc(COURIER_ID);
    }
}