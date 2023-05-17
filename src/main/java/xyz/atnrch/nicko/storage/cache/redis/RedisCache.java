package xyz.atnrch.nicko.storage.cache.redis;

import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.storage.cache.Cache;
import xyz.atnrch.nicko.storage.cache.CacheProvider;

import java.util.Optional;
import java.util.UUID;

public class RedisCache extends Cache {
    @Override
    public CacheProvider getProvider() {
        return null;
    }

    @Override
    public ActionResult<Void> cache(UUID uuid, NickoProfile profile) {
        return null;
    }

    @Override
    public boolean isCached(UUID uuid) {
        return false;
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        return Optional.empty();
    }
}
