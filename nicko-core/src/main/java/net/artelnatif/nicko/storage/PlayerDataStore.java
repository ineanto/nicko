package net.artelnatif.nicko.storage;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.artelnatif.nicko.storage.json.JSONStorage;
import net.artelnatif.nicko.storage.sql.SQLStorage;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataStore {
    private final Storage storage;

    public PlayerDataStore(NickoBukkit instance) {
        this.storage = instance.getNickoConfig().isLocalStorage() ? new JSONStorage() : new SQLStorage(instance);
    }

    public static final HashMap<UUID, NickoProfile> PROFILES = new HashMap<>();

    public Optional<NickoProfile> getData(UUID uuid) {
        if (storage.isError()) {
            return Optional.empty();
        }

        if (PROFILES.containsKey(uuid)) {
            return Optional.of(PROFILES.get(uuid));
        } else if (storage.isStored(uuid)) {
            final Optional<NickoProfile> retrievedProfile = storage.retrieve(uuid);
            retrievedProfile.ifPresent(profile -> PROFILES.put(uuid, profile));
            return retrievedProfile;
        } else {
            final NickoProfile newProfile = NickoProfile.EMPTY_PROFILE;
            PROFILES.put(uuid, newProfile);
            return Optional.of(newProfile);
        }
    }

    public Optional<NickoProfile> getOfflineData(String name) {
        if (storage.isError()) {
            return Optional.empty();
        }

        try {
            final Optional<String> uuidTrimmed = NickoBukkit.getInstance().getMojangAPI().getUUID(name);
            if (uuidTrimmed.isPresent()) {
                final UUID uuid = MojangUtils.fromTrimmed(uuidTrimmed.get());
                return getData(uuid);
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void saveData(Player player) {
        if (storage.isError()) {
            return;
        }

        storage.store(player.getUniqueId(), PROFILES.get(player.getUniqueId()));
        PROFILES.remove(player.getUniqueId());
    }

    public Storage getStorage() {
        return storage;
    }
}
