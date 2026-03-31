package com.ustenko.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(int id) {
        super("Ресторан с id=" + id + " не найден");
    }

    public RestaurantNotFoundException(String name) {
        super("Ресторан с названием '" + name + "' не найден");
    }
}
