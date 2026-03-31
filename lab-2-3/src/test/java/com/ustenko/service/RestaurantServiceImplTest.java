package com.ustenko.service;

import com.ustenko.dao.RestaurantRepository;
import com.ustenko.entity.RestaurantEntity;
import com.ustenko.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void findById_shouldReturnRestaurant_whenExists() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "Пушкинъ", 4.9);
        when(restaurantRepository.findById(1)).thenReturn(restaurant);

        RestaurantEntity result = restaurantService.findById(1);

        assertEquals(restaurant, result);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(restaurantRepository.findById(99)).thenReturn(null);

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.findById(99));
    }

    @Test
    void update_shouldThrowException_whenNotFound() {
        RestaurantEntity restaurant = new RestaurantEntity(99, "Нет такого", 1.0);
        when(restaurantRepository.update(restaurant)).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.update(restaurant));
    }

    @Test
    void findByName_shouldReturnRestaurant_whenExists() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "Белуга", 4.7);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        RestaurantEntity result = restaurantService.findByName("Белуга");

        assertEquals(restaurant, result);
    }
}
