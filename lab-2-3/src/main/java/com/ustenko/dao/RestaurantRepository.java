package com.ustenko.dao;

import com.ustenko.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantRepository {


    int save(RestaurantEntity restaurant);

    RestaurantEntity findById(int id);

    List<RestaurantEntity> findAll();


    boolean update(RestaurantEntity restaurant);

    void deleteById(int id);
}
