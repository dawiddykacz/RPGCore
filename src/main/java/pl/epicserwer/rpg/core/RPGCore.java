package pl.epicserwer.rpg.core;

import org.commons.Name;
import pl.epicserwer.rpg.core.experience.ChooseExperienceService;
import pl.epicserwer.rpg.core.experience.ExperienceService;
import pl.epicserwer.rpg.core.experience.required.RequiredExperienceService;
import pl.epicserwer.rpg.core.repositories.database.DatabaseConnection;
import pl.epicserwer.rpg.core.repositories.database.DatabaseDTO;
import pl.epicserwer.rpg.core.repositories.database.DatabaseType;

import java.sql.SQLException;

public class RPGCore {
    public static void main(String[] args) {
        final DatabaseConnection databaseConnection = new DatabaseConnection.
                Factory(DatabaseType.MYSQL, new DatabaseDTO(
                new Name("localhost"),
                3306,
                new Name("rpg_test_db"),
                new Name("test_user"),
                "test_pass"
        )).create();
        try {
            databaseConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
