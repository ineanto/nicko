package net.artelnatif.nicko.storage;

import net.artelnatif.nicko.disguise.NickoProfile;

import java.util.Optional;
import java.util.UUID;

public abstract class Storage {
    private boolean error = false;

    public abstract StorageProvider getProvider();

    public abstract void store(UUID uuid, NickoProfile profile);

    public abstract boolean isStored(UUID uuid);

    public abstract Optional<NickoProfile> retrieve(UUID uuid);

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
