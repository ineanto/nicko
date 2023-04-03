package net.artelnatif.nicko.storage.name;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerNameStore {
    private final HashMap<UUID, String> names = new HashMap<>();

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

    public void clearStoredNames() {
        names.clear();
    }
}
