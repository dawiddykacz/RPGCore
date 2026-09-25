package pl.epicserwer.rpg.core.repositories;

import org.commons.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

abstract class AbstractCashedRepository<V> {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<Name, V> cashed = new ConcurrentHashMap<>();
    private final Set<Name> dirtyKeys = new HashSet<>();

    private final ScheduledExecutorService autoSaveScheduler = Executors.newSingleThreadScheduledExecutor();

    public AbstractCashedRepository() {
        QuitRepositories quitRepositories = QuitRepositories.getInstance();
        quitRepositories.addRepository(this);
    }

    public V get(Name key) {
        V result = cashed.get(key);
        if (result == null) {
            load(key).join();
            result = cashed.get(key);
        }
        return result;
    }

    public void save(Name key, V value) {
        cashed.put(key, value);
        dirtyKeys.add(key);
    }

    void quit(Name key) {
        if(dirtyKeys.remove(key)) {
            try {
                saveToDb(key, cashed.get(key));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return;
            }
        }
        cashed.remove(key);
    }

    void shutdown(){
        saveAll();
    }

    private CompletableFuture<Void> load(Name key){
        return CompletableFuture.runAsync(() -> {
            try{
                V data = fetch(key);
                cashed.put(key, data != null ? data : getDefaultValue());
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        });
    }

    private void saveAll(){
        if(dirtyKeys.isEmpty()) return;

        Map<Name, V> batchToSave = new HashMap<>();
        Iterator<Name> iterator = dirtyKeys.iterator();

        while(iterator.hasNext()){
            Name key = iterator.next();
            V data = cashed.get(key);

            if(data != null){
                batchToSave.put(key, data);
            }
            iterator.remove();
        }

        if(batchToSave.isEmpty()) return;

        try{
            saveToDbAll(batchToSave);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            dirtyKeys.addAll(batchToSave.keySet());
        }
    }

    private void startAutoSaveTask(int autoSaveSecondsInterval){
        autoSaveScheduler.scheduleAtFixedRate(this::saveAll, autoSaveSecondsInterval, autoSaveSecondsInterval,
                TimeUnit.SECONDS);
    }

    protected abstract V fetch(Name key) throws Exception;
    abstract void saveToDb(Name key, V value) throws Exception;
    abstract void saveToDbAll(Map<Name, V> data) throws Exception;
    protected abstract V getDefaultValue();
}
