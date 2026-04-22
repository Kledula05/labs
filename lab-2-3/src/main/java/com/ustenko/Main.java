package com.ustenko;

import com.ustenko.dao.RestaurantRepository;
import com.ustenko.dao.JdbcRestaurantRepository;
import com.ustenko.db.ConnectionConfig;
import com.ustenko.entity.RestaurantEntity;
import com.ustenko.exception.RestaurantNotFoundException;
import com.ustenko.service.RestaurantService;
import com.ustenko.service.RestaurantServiceImpl;

import javax.sql.DataSource;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Запуск приложения Restaurant App ===");

        DataSource dataSource = new ConnectionConfig().createDataSource();
        RestaurantRepository restaurantRepository = new JdbcRestaurantRepository(dataSource);
        RestaurantService restaurantService = new RestaurantServiceImpl(restaurantRepository);

        demonstrateCrudOperations(restaurantService);

        System.out.println("=== Приложение завершило работу ===");
    }

    private static void demonstrateCrudOperations(RestaurantService restaurantService) {


        System.out.println("--- CREATE: сохранение ресторанов ---");

        int firstId = restaurantService.save("Пушкинъ", 4.9);
        System.out.println("Сохранён ресторан с id=" + firstId);

        int secondId = restaurantService.save("Белуга", 4.7);
        System.out.println("Сохранён ресторан с id=" + secondId);

        int thirdId = restaurantService.save("Матрёшка", 4.5);
        System.out.println("Сохранён ресторан с id=" + thirdId);


        System.out.println("--- READ: поиск по id ---");

        RestaurantEntity foundRestaurant = restaurantService.findById(firstId);
        System.out.println("Найден: " + foundRestaurant);


        System.out.println("--- READ: поиск по названию ---");

        RestaurantEntity byName = restaurantService.findByName("Белуга");
        System.out.println("Найден по названию: " + byName);

        System.out.println("--- READ: все рестораны ---");

        List<RestaurantEntity> allRestaurants = restaurantService.findAll();
        for (RestaurantEntity restaurant : allRestaurants) {
            System.out.println("  " + restaurant);
        }


        System.out.println("--- UPDATE: обновление ресторана ---");

        RestaurantEntity restaurantToUpdate = new RestaurantEntity(firstId, "Пушкинъ (обновлено)", 4.8);
        restaurantService.update(restaurantToUpdate);
        System.out.println("Обновление id=" + firstId + " выполнено");

        RestaurantEntity updatedRestaurant = restaurantService.findById(firstId);
        System.out.println("После обновления: " + updatedRestaurant);


        System.out.println("--- UPDATE несуществующего ресторана ---");
        try {
            restaurantService.update(new RestaurantEntity(999999, "Нет такого", 1.0));
        } catch (RestaurantNotFoundException e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }


        System.out.println("--- DELETE: удаление ресторана ---");

        restaurantService.deleteById(thirdId);
        System.out.println("Удалён ресторан id=" + thirdId);

        System.out.println("Рестораны после удаления:");
        restaurantService.findAll().forEach(restaurant -> System.out.println("  " + restaurant));


        System.out.println("--- findById для удалённого id=" + thirdId + " ---");
        try {
            restaurantService.findById(thirdId);
        } catch (RestaurantNotFoundException e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }
    }
}
