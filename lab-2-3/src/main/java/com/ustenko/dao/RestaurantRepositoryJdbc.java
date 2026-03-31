package com.ustenko.dao;

import com.ustenko.entity.RestaurantEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantRepositoryJdbc implements RestaurantRepository {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS restaurant (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "rating DOUBLE PRECISION NOT NULL" +
                    ")";

    private static final String SQL_INSERT =
            "INSERT INTO restaurant (name, rating) VALUES (?, ?) RETURNING id";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, rating FROM restaurant WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, name, rating FROM restaurant";

    private static final String SQL_UPDATE =
            "UPDATE restaurant SET name = ?, rating = ? WHERE id = ?";

    private static final String SQL_DELETE_BY_ID =
            "DELETE FROM restaurant WHERE id = ?";

    private final DataSource dataSource;

    public RestaurantRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public int save(RestaurantEntity restaurant) {
        // сохраняем ресторан и возвращаем его id
        int id = -1;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setString(1, restaurant.getName());
            statement.setDouble(2, restaurant.getRating());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public RestaurantEntity findById(int id) {
        // ищем ресторан по id
        RestaurantEntity restaurant = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                restaurant = new RestaurantEntity();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setRating(resultSet.getDouble("rating"));
            }

        } catch (SQLException e) {
            System.out.println("Ошибка поиска id=" + id + ": " + e.getMessage());
            e.printStackTrace();
        }

        return restaurant;
    }

    @Override
    public List<RestaurantEntity> findAll() {
        // получаем все рестораны из базы
        List<RestaurantEntity> restaurants = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                RestaurantEntity restaurant = new RestaurantEntity();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setRating(resultSet.getDouble("rating"));
                restaurants.add(restaurant);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }

    @Override
    public boolean update(RestaurantEntity restaurant) {
        // обновляем ресторан по id
        boolean updated = false;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, restaurant.getName());
            statement.setDouble(2, restaurant.getRating());
            statement.setInt(3, restaurant.getId());

            int rows = statement.executeUpdate();
            updated = rows > 0;

        } catch (SQLException e) {
            System.out.println("Ошибка обновления: " + e.getMessage());
            e.printStackTrace();
        }

        return updated;
    }

    @Override
    public void deleteById(int id) {
        // удаляем ресторан по id
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Ошибка удаления id=" + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
