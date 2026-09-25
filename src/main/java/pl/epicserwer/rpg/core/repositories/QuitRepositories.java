package pl.epicserwer.rpg.core.repositories;

import org.commons.Name;

import java.util.HashSet;
import java.util.Set;

public class QuitRepositories {
    private final Set<AbstractCashedRepository<?>> repositories;

    private static final class InstanceHolder {
        private static final QuitRepositories instance = new QuitRepositories();
    }

    public static QuitRepositories getInstance() {
        return InstanceHolder.instance;
    }

    private QuitRepositories() {
        this.repositories = new HashSet<>();
    }

    void addRepository(AbstractCashedRepository<?> repository) {
        this.repositories.add(repository);
    }

    void quit(Name key) {
        repositories.forEach(r -> r.quit(key));
    }

    void shutdown(){
        repositories.forEach(AbstractCashedRepository::shutdown);
    }


}
