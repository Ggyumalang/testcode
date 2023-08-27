package jocture.testcode.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

class MemberServiceManualContainerTest extends MemberServiceSpringBootTest {

    static PostgreSQLContainer<?> POSTGRES_CONTAINER;

    @BeforeAll
    static void setUp() {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:13")
                .withInitScript("member-init.sql");
        POSTGRES_CONTAINER.start();

        System.setProperty("spring.datasource.url", POSTGRES_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRES_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRES_CONTAINER.getPassword());
    }

    @AfterAll
    static void tearDown() {
        POSTGRES_CONTAINER.stop();
    }
}