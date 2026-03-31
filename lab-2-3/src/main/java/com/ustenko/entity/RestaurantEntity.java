package com.ustenko.entity;

public class RestaurantEntity {

    private Integer id;
    private String name;
    private double rating;

    public RestaurantEntity() {
    }

    public RestaurantEntity(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }

    public RestaurantEntity(Integer id, String name, double rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "RestaurantEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
