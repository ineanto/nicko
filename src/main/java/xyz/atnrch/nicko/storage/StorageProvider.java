package xyz.atnrch.nicko.storage;

public interface StorageProvider {
    boolean init();

    boolean close();
}
