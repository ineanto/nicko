package xyz.ineanto.nicko.storage;

public interface StorageProvider {
    boolean init();

    boolean close();
}
