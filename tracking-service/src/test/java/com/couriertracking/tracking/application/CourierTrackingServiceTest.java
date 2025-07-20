package com.couriertracking.tracking.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couriertracking.tracking.adapters.out.mongodb.CourierTravelRepository;
import com.couriertracking.tracking.adapters.out.mongodb.StoreEntryRepository;
import com.couriertracking.tracking.application.CourierTrackingService;
import com.couriertracking.tracking.domain.model.*;
import com.couriertracking.tracking.domain.service.DistanceCalculationStrategy;
import com.couriertracking.tracking.ports.out.StoreService;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierTrackingServiceTest {

        @Mock
        private CourierTravelRepository courierTravelRepository;

        @Mock
        private StoreEntryRepository storeEntryRepository;

        @Mock
        private StoreService storeService;

        @Mock
        private DistanceCalculationStrategy distanceCalculationStrategy;

        @InjectMocks
        private CourierTrackingService courierTrackingService;

        @Captor
        private ArgumentCaptor<CourierTravel> travelCaptor;

        @Captor
        private ArgumentCaptor<StoreEntry> entryCaptor;

        private CourierLocationEvent locationEvent;
        private Location location;
        private Store store;
        private CourierTravel existingTravel;

        private final String COURIER_ID = "courier123";
        private final double LAT = 40.9923307;
        private final double LNG = 29.1244229;
        private final String STORE_NAME = "Ataşehir Store";

        @BeforeEach
        void setUp() {
                location = new Location(LAT, LNG);
                Instant now = Instant.now();
                locationEvent = new CourierLocationEvent(COURIER_ID, LAT, LNG, now);

                store = new Store();
                store.setName(STORE_NAME);
                store.setLocation(new Location(LAT, LNG));

                Location previousLocation = new Location(LAT - 0.001, LNG - 0.001);
                existingTravel = new CourierTravel();
                existingTravel.setId("travel1");
                existingTravel.setCourierId(COURIER_ID);
                existingTravel.setFromLocation(previousLocation);
                existingTravel.setToLocation(previousLocation);
                existingTravel.setDistance(0.0);
        }

        @Test
        void shouldProcessLocationEventAndRecordTravel() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.of(existingTravel));
                when(distanceCalculationStrategy.calculateDistance(any(Location.class), any(Location.class)))
                                .thenReturn(150.0);
                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Collections.emptyList());

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(courierTravelRepository).findFirstByCourierId(COURIER_ID);
                verify(distanceCalculationStrategy).calculateDistance(any(Location.class), any(Location.class));
                verify(courierTravelRepository).save(travelCaptor.capture());

                CourierTravel savedTravel = travelCaptor.getValue();
                assertEquals(COURIER_ID, savedTravel.getCourierId());
                assertEquals(150.0, savedTravel.getDistance());
                assertEquals(location, savedTravel.getToLocation());
        }

        @Test
        void shouldCreateInitialTravelRecordWhenNoExistingTravel() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.empty());
                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Collections.emptyList());

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(courierTravelRepository).findFirstByCourierId(COURIER_ID);
                verify(courierTravelRepository).save(travelCaptor.capture());
                verify(distanceCalculationStrategy, never()).calculateDistance(any(Location.class),
                                any(Location.class));

                CourierTravel savedTravel = travelCaptor.getValue();
                assertEquals(COURIER_ID, savedTravel.getCourierId());
                assertEquals(0.0, savedTravel.getDistance());
                assertEquals(location, savedTravel.getFromLocation());
                assertEquals(location, savedTravel.getToLocation());
        }

        @Test
        void shouldIgnoreMinimalMovement() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.of(existingTravel));
                when(distanceCalculationStrategy.calculateDistance(any(Location.class), any(Location.class)))
                                .thenReturn(0.05);
                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Collections.emptyList());

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(courierTravelRepository).findFirstByCourierId(COURIER_ID);
                verify(distanceCalculationStrategy).calculateDistance(any(Location.class), any(Location.class));
                verify(courierTravelRepository, never()).save(any(CourierTravel.class));
        }

        @Test
        void shouldDetectStoreEntryAndSaveEntry() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.of(existingTravel));
                when(distanceCalculationStrategy.calculateDistance(any(Location.class), any(Location.class)))
                                .thenReturn(150.0);
                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Collections.singletonList(store));
                when(storeEntryRepository.findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(anyString(),
                                anyString(),
                                any(Instant.class)))
                                .thenReturn(Optional.empty());

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(storeService).findStoresWithinRadius(eq(location), eq(100.0));
                verify(storeEntryRepository).findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(
                                eq(COURIER_ID), eq(STORE_NAME), any(Instant.class));
                verify(storeEntryRepository).save(entryCaptor.capture());

                StoreEntry savedEntry = entryCaptor.getValue();
                assertEquals(COURIER_ID, savedEntry.getCourierId());
                assertEquals(STORE_NAME, savedEntry.getStoreName());
                assertNotNull(savedEntry.getEntryTime());
        }

        @Test
        void shouldIgnoreReentryWithinOneMinute() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.of(existingTravel));
                when(distanceCalculationStrategy.calculateDistance(any(Location.class), any(Location.class)))
                                .thenReturn(150.0);
                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Collections.singletonList(store));

                StoreEntry recentEntry = new StoreEntry();
                recentEntry.setCourierId(COURIER_ID);
                recentEntry.setStoreName(STORE_NAME);
                recentEntry.setEntryTime(Instant.now());

                when(storeEntryRepository.findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(anyString(),
                                anyString(),
                                any(Instant.class)))
                                .thenReturn(Optional.of(recentEntry));

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(storeService).findStoresWithinRadius(eq(location), eq(100.0));
                verify(storeEntryRepository).findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(
                                eq(COURIER_ID), eq(STORE_NAME), any(Instant.class));
                verify(storeEntryRepository, never()).save(any(StoreEntry.class));
        }

        @Test
        void shouldProcessMultipleNearbyStores() {
                // Given
                when(courierTravelRepository.findFirstByCourierId(anyString())).thenReturn(Optional.of(existingTravel));
                when(distanceCalculationStrategy.calculateDistance(any(Location.class), any(Location.class)))
                                .thenReturn(150.0);

                Store store2 = new Store();
                store2.setName("Another Store");
                store2.setLocation(new Location(LAT + 0.0001, LNG + 0.0001));

                when(storeService.findStoresWithinRadius(any(Location.class), anyDouble()))
                                .thenReturn(Arrays.asList(store, store2));
                when(storeEntryRepository.findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(anyString(),
                                anyString(),
                                any(Instant.class)))
                                .thenReturn(Optional.empty());

                // When
                courierTrackingService.processLocationEvent(locationEvent);

                // Then
                verify(storeService).findStoresWithinRadius(eq(location), eq(100.0));
                verify(storeEntryRepository, times(2)).findFirstByCourierIdAndStoreNameAndEntryTimeGreaterThan(
                                anyString(), anyString(), any(Instant.class));
                verify(storeEntryRepository, times(2)).save(any(StoreEntry.class));
        }
}