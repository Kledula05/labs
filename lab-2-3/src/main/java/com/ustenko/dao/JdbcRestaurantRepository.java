package com.ustenko.dao;

import com.ustenko.entity.RestaurantEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcRestaurantRepository implements RestaurantRepository {

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

    public JdbcRestaurantRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int save(RestaurantEntity restaurant) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setString(1, restaurant.getName());
            statement.setDouble(2, restaurant.getRating());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении ресторана", e);
        }

        throw new RuntimeException("Не удалось получить id после сохранения ресторана");
    }

    @Override
    public RestaurantEntity findById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    RestaurantEntity restaurant = new RestaurantEntity();
                    restaurant.setId(resultSet.getInt("id"));
                    restaurant.setName(resultSet.getString("name"));
                    restaurant.setRating(resultSet.getDouble("rating"));
                    return restaurant;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске ресторана по id=" + id, e);
        }

        return null;
    }

    @Override
    public List<RestaurantEntity> findAll() {
        List<RestaurantEntity> restaurants = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                RestaurantEntity restaurant = new RestaurantEntity();
                restaurant.setId(resultSet.getInt("id"));
                restaurant.setName(resultSet.getString("name"));
                restaurant.setRating(resultSet.getDouble("rating"));
                restaurants.add(restaurant);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка ресторанов", e);
        }

        return restaurants;
    }

    @Override
    public boolean update(RestaurantEntity restaurant) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, restaurant.getName());
            statement.setDouble(2, restaurant.getRating());
            statement.setInt(3, restaurant.getId());

            int rows = statement.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении ресторана id=" + restaurant.getId(), e);
        }
    }

    @Override
    public void deleteById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении ресторана id=" + id, e);
        }
    }
}
