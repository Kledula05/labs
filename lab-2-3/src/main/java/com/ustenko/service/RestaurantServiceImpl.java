package com.ustenko.service;

import com.ustenko.dao.RestaurantRepository;
import com.ustenko.entity.RestaurantEntity;
import com.ustenko.exception.RestaurantNotFoundException;

import java.util.List;

public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public int save(String name, double rating) {
        RestaurantEntity restaurant = new RestaurantEntity(name, rating);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public RestaurantEntity findById(int id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id);
        if (restaurant == null) {
            throw new RestaurantNotFoundException(id);
        }
        return restaurant;
    }

    @Override
    public RestaurantEntity findByName(String name) {
        List<RestaurantEntity> allRestaurants = restaurantRepository.findAll();
        for (RestaurantEntity restaurant : allRestaurants) {
            if (restaurant.getName().equals(name)) {
                return restaurant;
            }
        }
        throw new RestaurantNotFoundException(name);
    }

    @Override
    public List<RestaurantEntity> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public void update(RestaurantEntity restaurant) {
        boolean updated = restaurantRepository.update(restaurant);
        if (!updated) {
            throw new RestaurantNotFoundException(restaurant.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        restaurantRepository.deleteById(id);
    }
}
