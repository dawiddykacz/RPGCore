package pl.epicserwer.rpg.core.repositories;

import org.commons.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.epicserwer.rpg.core.repositories.database.DatabaseConnection;
import pl.epicserwer.rpg.core.repositories.database.DatabaseDTO;
import pl.epicserwer.rpg.core.repositories.database.DatabaseType;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Testcontainers
class AbstractSQLRepositoryTest {

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("rpg_test_db")
            .withUsername("test_user")
            .withPassword("test_pass");

    private TestStringRepository repository;

    @BeforeEach
    void setUp() {
        DatabaseDTO dto = new DatabaseDTO(
                new Name(mysql.getHost()),
                mysql.getFirstMappedPort(),
                new Name(mysql.getDatabaseName()),
                new Name(mysql.getUsername()),
                mysql.getPassword()
        );

        DatabaseConnection dbConnection = new DatabaseConnection.Factory(
                DatabaseType.MYSQL,
                new DatabaseDTO(new Name(mysql.getHost()),
                        mysql.getFirstMappedPort(),
                        new Name(mysql.getDatabaseName()),
                        new Name(mysql.getUsername()),
                        mysql.getPassword())
        ).create();

        repository = new TestStringRepository(dbConnection);
    }

    @Test
    void shouldCreateTableAndSaveSingleRecord() throws Exception {
        Name key = new Name("player1");
        String value = "WARRIOR";

        repository.saveToDb(key, value);

        String fetchedValue = repository.fetch(key);
        assertEquals("WARRIOR", fetchedValue, "The value fetched from the database should match the saved one");
    }

    @Test
    void shouldUpdateExistingRecordOnDuplicateKey() throws Exception {
        Name key = new Name("player2");
        repository.saveToDb(key, "MAGE");

        repository.saveToDb(key, "ARCHER");

        String fetchedValue = repository.fetch(key);
        assertEquals("ARCHER", fetchedValue, "The value in the database should be updated");
    }

    @Test
    void shouldSaveBatchRecordsToDatabase() throws Exception {
        Map<Name, String> batchData = new HashMap<>();
        batchData.put(new Name("player3"), "PALADIN");
        batchData.put(new Name("player4"), "ROGUE");
        batchData.put(new Name("player5"), "CLERIC");

        repository.saveToDbAll(batchData);

        assertEquals("PALADIN", repository.fetch(new Name("player3")));
        assertEquals("ROGUE", repository.fetch(new Name("player4")));
        assertEquals("CLERIC", repository.fetch(new Name("player5")));
    }

    @Test
    void shouldReturnNullWhenRecordDoesNotExist() throws Exception {
        Name notExistingKey = new Name("unknown_player");

        String fetchedValue = repository.fetch(notExistingKey);

        assertNull(fetchedValue, "Fetching a non-existent key should return null");
    }

    /**
     * A concrete implementation of AbstractSQLRepository for testing purposes.
     * It simply stores Strings (e.g., character classes mapped to player names).
     */
    private static class TestStringRepository extends AbstractSQLRepository<String> {

        private final DatabaseConnection dbConnection;

        public TestStringRepository(DatabaseConnection dbConnection) {
            super(dbConnection, "test_players",
                    "CREATE TABLE IF NOT EXISTS %s (name VARCHAR(255) PRIMARY KEY, player_class VARCHAR(255))");
            this.dbConnection = dbConnection;
        }

        @Override
        protected void setStatement(@Nonnull PreparedStatement statement,
                                    @Nonnull Name name,
                                    @Nonnull String value) throws SQLException {
            statement.setString(1, name.toString());
            statement.setString(2, value);
            statement.setString(3, value);
        }

        @Override
        protected String getUpdateSQL() {
            return "INSERT INTO test_players (name, player_class) VALUES (?, ?) ON DUPLICATE KEY UPDATE player_class = ?";
        }

        @Override
        protected String fetch(Name key) throws Exception {
            String sql = "SELECT player_class FROM test_players WHERE name = ?";
            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, key.toString());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("player_class");
                    }
                    return null;
                }
            }
        }

        @Override
        protected String getDefaultValue() {
            return "NONE";
        }
    }
}