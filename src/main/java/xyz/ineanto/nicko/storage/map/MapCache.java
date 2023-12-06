package xyz.ineanto.nicko.storage.map;

import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.Cache;
import xyz.ineanto.nicko.storage.CacheProvider;

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

    @Override
    public ActionResult delete(UUID uuid) {
        final HashMap<UUID, NickoProfile> profiles = provider.getMap();
        profiles.remove(uuid);
        return ActionResult.ok();
    }
}
