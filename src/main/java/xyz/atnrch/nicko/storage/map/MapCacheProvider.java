package xyz.atnrch.nicko.storage.map;

import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.CacheProvider;

import java.util.HashMap;
import java.util.UUID;

public class MapCacheProvider implements CacheProvider {
    private HashMap<UUID, NickoProfile> profiles;

    @Override
    public boolean init() {
        if (profiles == null) {
            profiles = new HashMap<>();
        }
        return true;
    }

    @Override
    public boolean close() {
        profiles = null;
        return true;
    }

    public HashMap<UUID, NickoProfile> getMap() {
        return profiles;
    }
}
