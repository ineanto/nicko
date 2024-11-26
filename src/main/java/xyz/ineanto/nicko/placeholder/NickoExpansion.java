package xyz.ineanto.nicko.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.profile.NickoProfile;

import java.util.Optional;

public class NickoExpansion extends PlaceholderExpansion {

    private final Nicko instance;

    public NickoExpansion(Nicko instance) {
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
        return "1.0.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;

        String name, skin, locale;
        boolean randomSkin;

        name = skin = player.getName();
        locale = "N/A";
        randomSkin = false;

        final Optional<NickoProfile> optionalProfile = instance.getDataStore().getData(player.getUniqueId());
        if (optionalProfile.isPresent()) {
            final NickoProfile profile = optionalProfile.get();
            name = profile.getName() == null ? player.getName() : profile.getName();
            skin = profile.getSkin() == null ? player.getName() : profile.getSkin();
            locale = profile.getLocale().getName();
            randomSkin = profile.isRandomSkin();
        }

        return switch (params) {
            case "name" -> name;
            case "skin" -> skin;
            case "locale" -> locale;
            case "random_skin" -> String.valueOf(randomSkin);
            default -> null;
        };
    }
}
