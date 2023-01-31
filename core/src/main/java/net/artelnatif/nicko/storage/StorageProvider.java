package net.artelnatif.nicko.storage;

public interface StorageProvider {
    boolean init();

    boolean close();
}
