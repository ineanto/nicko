package net.artelnatif.nicko.storage.sql;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.Storage;
import net.artelnatif.nicko.storage.StorageProvider;

import java.util.Optional;
import java.util.UUID;

public class SQLStorage extends Storage {
    private final NickoBukkit instance;

    public SQLStorage(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public StorageProvider getProvider() {
        return new SQLStorageProvider(instance);
    }

    @Override
    public void store(UUID uuid, NickoProfile profile) {
    }

    @Override
    public boolean isStored(UUID uuid) {
        return false;
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        return Optional.empty();
    }
}
