package pl.epicserwer.rpg.core.repositories.database;

import org.commons.Name;

public record DatabaseDTO(Name url, int port, Name database, Name username, String password) {
    public DatabaseDTO {
        if(port <= 0) throw new IllegalArgumentException("Port must be greater than 0");
    }
}
