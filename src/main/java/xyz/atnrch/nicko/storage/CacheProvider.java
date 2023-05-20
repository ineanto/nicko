package xyz.atnrch.nicko.storage;

public interface CacheProvider {
    boolean init();

    boolean close();
}
