package net.artelnatif.nicko.bukkit.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.artelnatif.nicko.bukkit.NickoBukkit;
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
        if (player == null) return null;

        String name, skin, locale;
        boolean bungeecord;

        name = skin = player.getName();
        locale = "N/A";
        bungeecord = true;

        final Optional<NickoProfile> optionalProfile = instance.getNicko().getDataStore().getData(player.getUniqueId());
        if (optionalProfile.isPresent()) {
            final NickoProfile profile = optionalProfile.get();
            if (!profile.isEmpty()) {
                name = profile.getName();
                skin = profile.getSkin();
            }
            locale = profile.getLocale().getName();
            bungeecord = profile.isBungeecordTransfer();
        }

        return switch (params) {
            case "name" -> name;
            case "skin" -> skin;
            case "locale" -> locale;
            case "bungeecord" -> String.valueOf(bungeecord);
            default -> null;
        };
    }
}
