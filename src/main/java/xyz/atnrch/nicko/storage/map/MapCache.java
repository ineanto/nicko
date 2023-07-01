package xyz.atnrch.nicko.storage.map;

import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.storage.Cache;
import xyz.atnrch.nicko.storage.CacheProvider;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class MapCache extends Cache {
    private MapCacheProvider provider;

    @Override
    public CacheProvider getProvider() {
        if (provider == null) {
            provider = new MapCacheProvider();
        }
        return provider;
    }

    @Override
    public ActionResult cache(UUID uuid, NickoProfile profile) {
        final HashMap<UUID, NickoProfile> profiles = provider.getMap();
        profiles.put(uuid, profile);
        return ActionResult.ok();
    }

    @Override
    public boolean isCached(UUID uuid) {
        final HashMap<UUID, NickoProfile> profiles = provider.getMap();
        return profiles.containsKey(uuid);
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        final HashMap<UUID, NickoProfile> profiles = provider.getMap();
        if (isCached(uuid)) {
            return Optional.of(profiles.get(uuid));
        }
        return Optional.empty();
    }
}
