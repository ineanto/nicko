package xyz.atnrch.nicko.i18n;

import com.github.jsixface.YamlConfig;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.NickoProfile;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Optional;

public class I18N {
    private final MessageFormat formatter = new MessageFormat("");
    private final Player player;
    private final Locale playerLocale;

    public I18N(Player player) {
        this.player = player;
        this.playerLocale = getPlayerLocale();
    }

    public String translate(String key, Object... arguments) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final String string = readString(key);

        try {
            formatter.applyPattern(string);
            return instance.getNickoConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return instance.getNickoConfig().getPrefix() + key;
        }
    }

    public String translateWithoutPrefix(String key, Object... arguments) {
        final String translation = readString(key);
        try {
            formatter.applyPattern(translation);
            return formatter.format(arguments);
        } catch (Exception e) {
            return key;
        }
    }

    private String readString(String key) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        String string;
        if (playerLocale == Locale.CUSTOM) {
            string = instance.getLocaleFileManager().get(key);
        } else {
            final InputStream resource = instance.getResource(playerLocale.getCode() + ".yml");
            final YamlConfig yamlConfig = YamlConfig.load(resource);
            string = yamlConfig.getString(key);
        }

        return string;
    }

    private Locale getPlayerLocale() {
        final NickoBukkit instance = NickoBukkit.getInstance();
        try {
            final Optional<NickoProfile> profile = instance.getDataStore().getData(player.getUniqueId());
            return !profile.isPresent() ? Locale.FALLBACK_LOCALE : profile.get().getLocale();
        } catch (IllegalArgumentException exception) {
            instance.getLogger().severe("Invalid locale provided by " + player.getName() + ", defaulting to " + Locale.FALLBACK_LOCALE.getCode() + ".");
            return Locale.FALLBACK_LOCALE;
        }
    }
}
