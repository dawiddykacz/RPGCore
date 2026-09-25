package pl.epicserwer.rpg.core.repositories.database;

public enum DatabaseType {
    MYSQL("mysql");

    private final String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public static DatabaseType getDatabaseType(String type) {
        for (DatabaseType value : values()) {
            if (value.name.equalsIgnoreCase(type)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown database type: " + type);
    }

    @Override
    public String toString() {
        return name;
    }
}
