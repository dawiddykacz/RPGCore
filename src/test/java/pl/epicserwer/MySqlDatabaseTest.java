package pl.epicserwer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers // Włącza automatyczne zarządzanie kontenerami przez Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MySqlDatabaseTest {

    // Definiujemy oficjalny obraz MySQL. Wersję dobierz do swojej produkcyjnej.
    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private HikariDataSource dataSource;

    @BeforeAll
    void setupDataSource() {
        // Kontener w tym momencie już działa. Pobieramy dynamiczny URL i dane logowania.
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(mysql.getJdbcUrl());
        config.setUsername(mysql.getUsername());
        config.setPassword(mysql.getPassword());
        config.setDriverClassName(mysql.getDriverClassName());
        config.setMaximumPoolSize(5);

        this.dataSource = new HikariDataSource(config);
    }

    @BeforeEach
    void setupTable() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            // Przygotowanie czystej tabeli przed każdym testem
            stmt.execute("DROP TABLE IF EXISTS users");
            stmt.execute("CREATE TABLE users (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))");
        }
    }

    @AfterAll
    void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
        // Kontener mysql zamknie się automatycznie dzięki adnotacji @Testcontainers
    }

    @Test
    @DisplayName("Powinien poprawnie zapisać użytkownika w prawdziwym MySQL")
    void shouldInsertUserIntoMySql() throws SQLException {
        // GIVEN
        String name = "Cezary";

        // WHEN
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO users (name) VALUES ('" + name + "')");
        }

        // THEN
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM users WHERE name = '" + name + "'")) {

            assertTrue(rs.next());
            assertEquals(name, rs.getString("name"));
        }
    }
}