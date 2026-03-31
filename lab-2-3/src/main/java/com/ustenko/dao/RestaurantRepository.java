package com.ustenko.dao;

import com.ustenko.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantRepository {

    // Create  возвращает id созданной записи
    int save(RestaurantEntity restaurant);

    // Read  возвращает null, если запись не найдена
    RestaurantEntity findById(int id);

    List<RestaurantEntity> findAll();

    // Update  возвращает false, если запись не найдена
    boolean update(RestaurantEntity restaurant);

    // Delete
    void deleteById(int id);
}
