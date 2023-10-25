package com.example.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@Testcontainers
@SpringBootTest(classes = NotificationServiceApplicationTest.class)
class NotificationServiceApplicationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

//    @DynamicPropertySource
//    static void postgresSqlProperties(DynamicPropertyRegistry registry){
//        registry.add("postgres.driver", postgreSQLContainer::getDriverClassName);
//    }
//
//    try (Connection conn = DriverManager.getConnection(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
//    Statement stmt = conn.createStatement()) {
//        stmt.execute("CREATE SCHEMA notification_bot");
//    }
//
//    NotificationServiceApplicationTest() throws SQLException {
//    }

    @Test
    void contextLoads() {
    }
}