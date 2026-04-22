package com.ustenko.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class ConnectionConfig {

    public DataSource createDataSource() {
        Properties properties = loadProperties();


        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));

        String poolSize = properties.getProperty("db.pool.size", "5");
        hikariConfig.setMaximumPoolSize(Integer.parseInt(poolSize));
        hikariConfig.setPoolName("restaurant-app-pool");

        System.out.println("Инициализация пула соединений HikariCP...");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);


        try {
            initializeLiquibase(dataSource);
            System.out.println("Liquibase: таблицы созданы успешно.");
        } catch (Exception e) {
            System.out.println("Ошибка Liquibase: " + e.getMessage());
            e.printStackTrace();
        }

        return dataSource;
    }

    private void initializeLiquibase(DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update();
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("Файл db.properties не найден в classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось загрузить db.properties", e);
        }

        System.out.println("Настройки подключения загружены.");
        return properties;
    }
}
