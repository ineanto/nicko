package net.artelnatif.nicko.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NickoExpansion extends PlaceholderExpansion {

    private final NickoBukkit instance;

    public NickoExpansion(NickoBukkit instance) {
        this.instance = instance;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nicko";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Aro";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        Optional<NickoProfile> optionalProfile = instance.getDataStore().getData(player.getUniqueId());
        if (optionalProfile.isPresent()) {
            final NickoProfile profile = optionalProfile.get();
            return switch (params) {
                case "name" -> profile.getName();
                case "skin" -> profile.getSkin();
                case "locale" -> profile.getLocale().getName();
                case "bungeecord" -> String.valueOf(profile.isBungeecordTransfer());
                default -> null;
            };
        } else {
            instance.getLogger().severe("Couldn't satisfy request for placeholder " + params + ". This is a bug!");
            return null;
        }
    }
}
