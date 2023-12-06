package xyz.ineanto.nicko.storage.map;

import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.CacheProvider;

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
        return true;
    }

    public HashMap<UUID, NickoProfile> getMap() {
        return profiles;
    }
}
