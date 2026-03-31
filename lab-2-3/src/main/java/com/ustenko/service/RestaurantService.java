package com.ustenko.service;

import com.ustenko.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantService {

    // Create  возвращает id созданной записи
    int save(String name, double rating);

    // Read (бросает исключение, если запись не найдена)
    RestaurantEntity findById(int id);

    // Read (бросает исключение, если запись не найдена)
    RestaurantEntity findByName(String name);

    List<RestaurantEntity> findAll();

    // Update (бросает исключение, если запись не найдена)
    void update(RestaurantEntity restaurant);

    // Delete
    void deleteById(int id);
}
