package net.artelnatif.nicko.storage;

import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.artelnatif.nicko.storage.json.JSONStorage;
import net.artelnatif.nicko.storage.sql.SQLStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataStore {
    private final Storage storage;
    private final HashMap<UUID, NickoProfile> profiles = new HashMap<>();
    private final HashMap<UUID, String> names = new HashMap<>();

    public PlayerDataStore(Nicko nicko) {
        this.storage = nicko.getConfig().local() ? new JSONStorage(nicko) : new SQLStorage(nicko);
    }

    public void storeName(Player player) {
        if (!isNameStored(player)) {
            names.put(player.getUniqueId(), player.getName());
        }
    }

    public String getStoredName(Player player) {
        return names.get(player.getUniqueId());
    }

    private boolean isNameStored(Player player) {
        return names.containsKey(player.getUniqueId());
    }

    public void removeAllNames() {
        names.clear();
    }

    public void saveAll() {
        Bukkit.getOnlinePlayers().forEach(this::saveData);
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
        if (storage.isError()) { return; }
        if (!profiles.containsKey(player.getUniqueId())) { return; }

        storage.store(player.getUniqueId(), profiles.get(player.getUniqueId()));
        profiles.remove(player.getUniqueId());
    }

    public Storage getStorage() {
        return storage;
    }
}
