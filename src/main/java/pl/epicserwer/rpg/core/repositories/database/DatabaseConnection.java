package pl.epicserwer.rpg.core.repositories.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    public static class Factory{
        private final String databaseUrl;
        private final DatabaseDTO databaseDTO;

        public Factory(DatabaseType databaseType, DatabaseDTO databaseDTO){
            this.databaseDTO = databaseDTO;
            this.databaseUrl = "jdbc:"+databaseType+"://"+databaseDTO.url()+":"+databaseDTO.port()
                    +"/"+databaseDTO.database()+"?useSSL=false&allowPublicKeyRetrieval=true";
        }

        public DatabaseConnection create(){
            return new DatabaseConnection(databaseUrl, databaseDTO);
        }
    }

    private final HikariDataSource hikariDataSource;

    private DatabaseConnection(@Nonnull final String databaseUrl, @Nonnull final DatabaseDTO databaseDTO) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(databaseUrl);
        config.setUsername(databaseDTO.username().toString());
        config.setPassword(databaseDTO.password());

        config.setMaximumPoolSize(10);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        hikariDataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
