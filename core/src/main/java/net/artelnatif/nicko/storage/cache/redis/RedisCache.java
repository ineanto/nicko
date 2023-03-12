package net.artelnatif.nicko.storage.cache.redis;

import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.cache.Cache;
import net.artelnatif.nicko.storage.cache.CacheProvider;

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
