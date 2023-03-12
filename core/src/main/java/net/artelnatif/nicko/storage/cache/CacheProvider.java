package net.artelnatif.nicko.storage.cache;

public interface CacheProvider {
    boolean init();

    boolean close();
}
