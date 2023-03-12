package net.artelnatif.nicko.storage;

import net.artelnatif.nicko.config.Configuration;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.artelnatif.nicko.storage.cache.Cache;
import net.artelnatif.nicko.storage.cache.redis.RedisCache;
import net.artelnatif.nicko.storage.json.JSONStorage;
import net.artelnatif.nicko.storage.sql.SQLStorage;
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
