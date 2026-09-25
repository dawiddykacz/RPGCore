package pl.epicserwer.rpg.core.repositories;

import org.commons.Name;
import pl.epicserwer.rpg.core.repositories.database.DatabaseConnection;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public abstract class AbstractSQLRepository<V> extends AbstractCashedRepository<V>{
    private final DatabaseConnection databaseConnection;
    protected final String tableName;

    public AbstractSQLRepository(DatabaseConnection databaseConnection, String tableName, String createTableSQL) {
        this.databaseConnection = databaseConnection;
        this.tableName = tableName;

        try {
            createTable(createTableSQL);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected abstract void setStatement(@Nonnull final PreparedStatement statement,
                                         @Nonnull final Name name,
                                         @Nonnull final V value) throws SQLException;
    protected abstract String getUpdateSQL();

    @Override
    void saveToDb(Name key, V value) throws Exception {
        try(Connection connection = databaseConnection.getConnection()){
            PreparedStatement statement = connection.prepareStatement(getUpdateSQL());

            setStatement(statement, key, value);
            statement.executeUpdate();
        }
    }

    @Override
    void saveToDbAll(Map<Name, V> data) throws Exception {
        try(Connection connection = databaseConnection.getConnection()){
            PreparedStatement statement = connection.prepareStatement(getUpdateSQL());

            for (Map.Entry<Name, V> nameVEntry : data.entrySet()) {
                setStatement(statement, nameVEntry.getKey(), nameVEntry.getValue());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void createTable(String sql) throws SQLException {
        sql = sql.formatted(tableName);

        try(Connection con = databaseConnection.getConnection()) {
            con.createStatement().execute(sql);
        }
    }
}
