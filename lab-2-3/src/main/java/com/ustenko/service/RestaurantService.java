package com.ustenko.service;

import com.ustenko.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantService {


    int save(String name, double rating);

    RestaurantEntity findById(int id);

    RestaurantEntity findByName(String name);

    List<RestaurantEntity> findAll();

    void update(RestaurantEntity restaurant);

    void deleteById(int id);
}
