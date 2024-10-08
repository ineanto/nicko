package xyz.ineanto.nicko.storage;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.ineanto.nicko.mojang.MojangUtils;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.json.JSONStorage;
import xyz.ineanto.nicko.storage.map.MapCache;
import xyz.ineanto.nicko.storage.mariadb.MariaDBStorage;
import xyz.ineanto.nicko.storage.mysql.MySQLStorage;
import xyz.ineanto.nicko.storage.redis.RedisCache;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class PlayerDataStore {
    private final MojangAPI mojangAPI;

    private Storage storage;
    private Cache cache;

    public PlayerDataStore(MojangAPI mojangAPI, Configuration configuration) {
        this.mojangAPI = mojangAPI;
        this.storage = configuration.getSqlConfiguration().isEnabled() ?
                configuration.getSqlConfiguration().isMariadb() ? new MariaDBStorage(configuration) : new MySQLStorage(configuration)
                : new JSONStorage();
        this.cache = configuration.getRedisConfiguration().isEnabled() ? new RedisCache(configuration) : new MapCache();
    }

    public ActionResult updateCache(UUID uuid, NickoProfile profile) {
        if (storage.isError() || cache.isError()) {
            return ActionResult.error(LanguageKey.Error.CACHE);
        }

        getCache().cache(uuid, profile);
        return ActionResult.ok();
    }

    public Optional<NickoProfile> getData(UUID uuid) {
        if (storage.isError() || cache.isError()) {
            return Optional.empty();
        }

        if (cache.isCached(uuid)) {
            return cache.retrieve(uuid);
        } else if (storage.isStored(uuid)) {
            final Optional<NickoProfile> retrievedProfile = storage.retrieve(uuid);
            retrievedProfile.ifPresent(profile -> cache.cache(uuid, profile));
            return retrievedProfile;
        } else {
            final NickoProfile newProfile = NickoProfile.EMPTY_PROFILE.clone();
            cache.cache(uuid, newProfile);
            return Optional.of(newProfile);
        }
    }

    public Optional<NickoProfile> getOfflineData(String name) {
        if (storage.isError() || cache.isError()) {
            return Optional.empty();
        }

        try {
            final Optional<String> uuidTrimmed = mojangAPI.getUUID(name);
            if (uuidTrimmed.isPresent()) {
                final UUID uuid = MojangUtils.fromTrimmed(uuidTrimmed.get());
                return getData(uuid);
            }
            return Optional.empty();
        } catch (IOException | ExecutionException e) {
            return Optional.empty();
        }
    }

    public ActionResult saveData(Player player) {
        if (storage.isError()) return ActionResult.error();
        if (cache.isError()) return ActionResult.error();
        if (!cache.isCached(player.getUniqueId())) return ActionResult.error();

        final Optional<NickoProfile> cachedProfile = cache.retrieve(player.getUniqueId());
        if (cachedProfile.isEmpty()) return ActionResult.error();

        cache.delete(player.getUniqueId());
        return storage.store(player.getUniqueId(), cachedProfile.get());
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }
}
