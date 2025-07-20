package com.couriertracking.courier.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.couriertracking.courier.adapters.out.mongodb.CourierRepository;
import com.couriertracking.courier.domain.model.Courier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierService courierService;

    private Courier testCourier;

    @BeforeEach
    void setUp() {
        testCourier = new Courier();
        testCourier.setId("courier123");
        testCourier.setName("Test Courier");
        testCourier.setPhoneNumber("+905551234567");
        testCourier.setActive(true);
    }

    @Test
    void shouldCreateCourier() {
        // Given
        when(courierRepository.save(any(Courier.class))).thenReturn(testCourier);

        // When
        Courier result = courierService.createCourier(testCourier);

        // Then
        assertNotNull(result);
        assertEquals("courier123", result.getId());
        assertEquals("Test Courier", result.getName());
        verify(courierRepository, times(1)).save(any(Courier.class));
    }

    @Test
    void shouldGetCourierById() {
        // Given
        when(courierRepository.findById(anyString())).thenReturn(Optional.of(testCourier));

        // When
        Optional<Courier> result = courierService.getCourierById("courier123");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Courier", result.get().getName());
        verify(courierRepository, times(1)).findById("courier123");
    }

    @Test
    void shouldReturnEmptyWhenCourierNotFound() {
        // Given
        when(courierRepository.findById(anyString())).thenReturn(Optional.empty());

        // When
        Optional<Courier> result = courierService.getCourierById("nonexistent");

        // Then
        assertFalse(result.isPresent());
        verify(courierRepository, times(1)).findById("nonexistent");
    }

    @Test
    void shouldGetAllCouriers() {
        // Given
        Courier courier2 = new Courier();
        courier2.setId("courier456");
        courier2.setName("Another Courier");

        when(courierRepository.findAll()).thenReturn(Arrays.asList(testCourier, courier2));

        // When
        List<Courier> result = courierService.getAllCouriers();

        // Then
        assertEquals(2, result.size());
        assertEquals("courier123", result.get(0).getId());
        assertEquals("courier456", result.get(1).getId());
        verify(courierRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateCourier() {
        // Given
        when(courierRepository.existsById(anyString())).thenReturn(true);
        when(courierRepository.save(any(Courier.class))).thenReturn(testCourier);

        // When
        Courier updatedCourier = new Courier();
        updatedCourier.setName("Updated Name");
        Courier result = courierService.updateCourier("courier123", updatedCourier);

        // Then
        assertEquals("courier123", result.getId());
        verify(courierRepository, times(1)).existsById("courier123");
        verify(courierRepository, times(1)).save(any(Courier.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentCourier() {
        // Given
        when(courierRepository.existsById(anyString())).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            courierService.updateCourier("nonexistent", new Courier());
        });
        verify(courierRepository, times(1)).existsById("nonexistent");
        verify(courierRepository, never()).save(any(Courier.class));
    }

    @Test
    void shouldActivateCourier() {
        // Given
        Courier inactiveCourier = new Courier();
        inactiveCourier.setId("courier123");
        inactiveCourier.setActive(false);

        when(courierRepository.findById(anyString())).thenReturn(Optional.of(inactiveCourier));
        when(courierRepository.save(any(Courier.class))).thenReturn(testCourier);

        // When
        courierService.activateCourier("courier123");

        // Then
        verify(courierRepository, times(1)).findById("courier123");
        verify(courierRepository, times(1)).save(any(Courier.class));
        assertTrue(inactiveCourier.isActive());
    }

    @Test
    void shouldDeactivateCourier() {
        // Given
        when(courierRepository.findById(anyString())).thenReturn(Optional.of(testCourier));
        when(courierRepository.save(any(Courier.class))).thenReturn(testCourier);

        // When
        courierService.deactivateCourier("courier123");

        // Then
        verify(courierRepository, times(1)).findById("courier123");
        verify(courierRepository, times(1)).save(any(Courier.class));
        assertFalse(testCourier.isActive());
    }
}