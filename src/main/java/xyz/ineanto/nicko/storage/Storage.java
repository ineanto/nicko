package xyz.ineanto.nicko.storage;

import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.util.Optional;
import java.util.UUID;

public abstract class Storage {
    private boolean error = false;

    public abstract StorageProvider getProvider();

    public abstract ActionResult store(UUID uuid, NickoProfile profile);

    public abstract boolean isStored(UUID uuid);

    public abstract Optional<NickoProfile> retrieve(UUID uuid);

    public abstract ActionResult delete(UUID uuid);

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
