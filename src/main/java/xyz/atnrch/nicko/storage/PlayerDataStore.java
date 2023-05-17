package xyz.atnrch.nicko.storage;

import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.disguise.NickoProfile;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.atnrch.nicko.mojang.MojangUtils;
import xyz.atnrch.nicko.storage.cache.Cache;
import xyz.atnrch.nicko.storage.cache.redis.RedisCache;
import xyz.atnrch.nicko.storage.json.JSONStorage;
import xyz.atnrch.nicko.storage.sql.SQLStorage;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataStore {
    private final Storage storage;
    private final Cache cache;
    private final MojangAPI mojangAPI;
    private final HashMap<UUID, NickoProfile> profiles = new HashMap<>();

    public PlayerDataStore(MojangAPI mojangAPI, Configuration configuration) {
        this.mojangAPI = mojangAPI;
        this.storage = configuration.isLocal() ? new JSONStorage() : new SQLStorage(configuration);
        this.cache = new RedisCache(); // The only option for now!
    }

    public void performProfileUpdate(UUID uuid, NickoProfile profile) {
        if (!profiles.containsKey(uuid)) {
            profiles.put(uuid, profile);
            return;
        }

        profiles.replace(uuid, profile);
    }

    public Optional<NickoProfile> getData(UUID uuid) {
        if (storage.isError()) {
            return Optional.empty();
        }

        if (profiles.containsKey(uuid)) {
            return Optional.of(profiles.get(uuid));
        } else if (storage.isStored(uuid)) {
            final Optional<NickoProfile> retrievedProfile = storage.retrieve(uuid);
            retrievedProfile.ifPresent(profile -> profiles.put(uuid, profile));
            return retrievedProfile;
        } else {
            final NickoProfile newProfile = NickoProfile.EMPTY_PROFILE.clone();
            profiles.put(uuid, newProfile);
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

    public ActionResult<Void> saveData(Player player) {
        if (storage.isError()) { return new ActionResult<>(I18NDict.Error.UNEXPECTED_ERROR); }
        if (!profiles.containsKey(player.getUniqueId())) { return new ActionResult<>(I18NDict.Error.UNEXPECTED_ERROR); }

        final ActionResult<Void> store = storage.store(player.getUniqueId(), profiles.get(player.getUniqueId()));
        profiles.remove(player.getUniqueId());
        return store;
    }

    public Storage getStorage() {
        return storage;
    }

    public Cache getCache() {
        return cache;
    }
}
