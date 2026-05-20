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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @Test
    void save_shouldReturnId_whenSuccessful() {
        // когда у restaurantRepository будет вызван save с любым RestaurantEntity тогда верни 1 :)
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(1);

        int result = restaurantService.save("Пушкинъ", 4.9);

        assertEquals(1, result);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

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
    void findByName_shouldReturnRestaurant_whenExists() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "Белуга", 4.7);
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        RestaurantEntity result = restaurantService.findByName("Белуга");

        assertEquals(restaurant, result);
    }

    @Test
    void findByName_shouldThrowException_whenNotFound() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.findByName("Несуществующий"));
    }

    @Test
    void findAll_shouldReturnList() {
        RestaurantEntity r1 = new RestaurantEntity(1, "A", 4.0);
        RestaurantEntity r2 = new RestaurantEntity(2, "B", 4.5);
        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));

        List<RestaurantEntity> result = restaurantService.findAll();

        assertEquals(2, result.size());
        verify(restaurantRepository).findAll();
    }

    @Test
    void update_shouldNotThrow_whenSuccessful() {
        RestaurantEntity restaurant = new RestaurantEntity(1, "Обновлённый", 4.8);
        when(restaurantRepository.update(any(RestaurantEntity.class))).thenReturn(true);

        restaurantService.update(restaurant);

        verify(restaurantRepository).update(any(RestaurantEntity.class));
    }

    @Test
    void update_shouldThrowException_whenNotFound() {
        RestaurantEntity restaurant = new RestaurantEntity(99, "Нет такого", 1.0);
        when(restaurantRepository.update(any(RestaurantEntity.class))).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> restaurantService.update(restaurant));
    }

    @Test
    void deleteById_shouldCallRepository() {
        restaurantService.deleteById(5);

        verify(restaurantRepository).deleteById(5);
    }
}