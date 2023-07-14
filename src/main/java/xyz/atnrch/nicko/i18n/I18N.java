package xyz.atnrch.nicko.i18n;

import com.github.jsixface.YamlConfig;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.AppearanceManager;

import java.io.InputStream;
import java.text.MessageFormat;

public class I18N {
    private final MessageFormat formatter = new MessageFormat("");
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final Player player;
    private final Locale playerLocale;

    public I18N(Player player) {
        this.player = player;
        this.playerLocale = getPlayerLocale();
    }

    public String translate(String key, Object... arguments) {
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
        try {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);
            return !appearanceManager.hasData() ? Locale.FALLBACK_LOCALE : appearanceManager.getLocale();
        } catch (IllegalArgumentException exception) {
            instance.getLogger().severe("Invalid locale provided by " + player.getName() + ", defaulting to " + Locale.FALLBACK_LOCALE.getCode() + ".");
            return Locale.FALLBACK_LOCALE;
        }
    }
}
