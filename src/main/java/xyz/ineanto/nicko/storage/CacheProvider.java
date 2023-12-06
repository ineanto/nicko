package xyz.ineanto.nicko.storage;

public interface CacheProvider {
    boolean init();

    boolean close();
}
