package xyz.atnrch.nicko.storage.json;

import xyz.atnrch.nicko.storage.StorageProvider;

import java.io.File;

public class JSONStorageProvider implements StorageProvider {
    private final File directory;

    public JSONStorageProvider(File directory) {
        this.directory = directory;
    }

    @Override
    public boolean init()
    {
        return directory.exists() || directory.mkdirs();
    }

    @Override
    public boolean close() { return true; }
}
