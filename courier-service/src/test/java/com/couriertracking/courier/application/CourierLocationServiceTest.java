package com.couriertracking.courier.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couriertracking.courier.application.CourierLocationService;
import com.couriertracking.courier.domain.model.CourierLocationData;
import com.couriertracking.courier.domain.model.CourierLocationEvent;
import com.couriertracking.courier.domain.model.Location;
import com.couriertracking.courier.ports.out.EventPublisher;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierLocationServiceTest {

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CourierLocationService courierLocationService;

    @Captor
    private ArgumentCaptor<CourierLocationEvent> eventCaptor;

    private CourierLocationData locationData;
    private final String COURIER_ID = "courier123";
    private final double LAT = 40.9923307;
    private final double LNG = 29.1244229;

    @BeforeEach
    void setUp() {
        Instant now = Instant.now();
        Location location = new Location(LAT, LNG);
        locationData = new CourierLocationData(COURIER_ID, location, now);
    }

    @Test
    void shouldReportLocationAndPublishEvent() {
        // When
        courierLocationService.reportLocation(locationData);

        // Then
        verify(eventPublisher).publishLocationEvent(any(CourierLocationEvent.class));
    }

    @Test
    void shouldThrowExceptionWhenPublishFails() {
        // Given
        doThrow(new RuntimeException("Publish failed")).when(eventPublisher)
                .publishLocationEvent(any(CourierLocationEvent.class));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            courierLocationService.reportLocation(locationData);
        });
    }
}