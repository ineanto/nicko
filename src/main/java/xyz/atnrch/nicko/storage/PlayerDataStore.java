package xyz.atnrch.nicko.storage;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.mojang.MojangUtils;
import xyz.atnrch.nicko.storage.json.JSONStorage;
import xyz.atnrch.nicko.storage.map.MapCache;
import xyz.atnrch.nicko.storage.redis.RedisCache;
import xyz.atnrch.nicko.storage.sql.SQLStorage;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataStore {
    private final Storage storage;
    private final Cache cache;
    private final MojangAPI mojangAPI;

    public PlayerDataStore(MojangAPI mojangAPI, Configuration configuration) {
        this.mojangAPI = mojangAPI;
        this.storage = configuration.getSqlConfiguration().isEnabled() ? new SQLStorage(configuration) : new JSONStorage();
        this.cache = configuration.getRedisConfiguration().isEnabled() ? new RedisCache(configuration) : new MapCache();
    }

    public Optional<NickoProfile> getData(UUID uuid) {
        if (storage.isError()) {
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
        if (storage.isError()) {
            return Optional.empty();
        }

        try {
            final Optional<String> uuidTrimmed = mojangAPI.getUUID(name);
            if (uuidTrimmed.isPresent()) {
                final UUID uuid = MojangUtils.fromTrimmed(uuidTrimmed.get());
                return getData(uuid);
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public ActionResult saveData(Player player) {
        if (storage.isError()) return ActionResult.error(I18NDict.Error.GENERIC);
        if (!cache.isCached(player.getUniqueId())) return ActionResult.error(I18NDict.Error.GENERIC);
        if (!cache.retrieve(player.getUniqueId()).isPresent())
            return ActionResult.error(I18NDict.Error.GENERIC);

        // TODO (Ineanto, 5/20/23): Remove value from cache
        //profiles.remove(player.getUniqueId());
        return storage.store(player.getUniqueId(), cache.retrieve(player.getUniqueId()).get());
    }

    public Storage getStorage() {
        return storage;
    }

    public Cache getCache() {
        return cache;
    }
}
