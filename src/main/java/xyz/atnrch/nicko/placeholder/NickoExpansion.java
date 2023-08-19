package xyz.atnrch.nicko.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.profile.AppearanceData;
import xyz.atnrch.nicko.profile.NickoProfile;

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
        return "Ineanto";
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

        final Optional<NickoProfile> optionalProfile = instance.getDataStore().getData(player.getUniqueId());
        if (optionalProfile.isPresent()) {
            final NickoProfile profile = optionalProfile.get();
            final AppearanceData appearanceData = profile.getAppearanceData();
            if (!appearanceData.isEmpty()) {
                name = appearanceData.getName();
                skin = appearanceData.getSkin();
            }
            locale = profile.getLocale().getName();
            bungeecord = profile.isBungeecordTransfer();
        }

        switch (params) {
            case "name":
                return name;
            case "skin":
                return skin;
            case "locale":
                return locale;
            case "bungeecord":
                return String.valueOf(bungeecord);
            default:
                return null;
        }
    }
}
