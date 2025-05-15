package com.javarush.jira;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest()
@ActiveProfiles("test")
abstract class BaseTestsWithTestcontainers {

    @Container
    static final PostgreSQLContainer<?> postreSQL = new PostgreSQLContainer<>("postgres");

    static {
        postreSQL.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postreSQL::getJdbcUrl);
        registry.add("spring.datasource.username", postreSQL::getUsername);
        registry.add("spring.datasource.password", postreSQL::getPassword);
    }
}