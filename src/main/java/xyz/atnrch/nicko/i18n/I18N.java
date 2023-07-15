package xyz.atnrch.nicko.i18n;

import com.github.jsixface.YamlConfig;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.AppearanceManager;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class I18N {
    private final MessageFormat formatter = new MessageFormat("");
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final Player player;
    private final Locale playerLocale;

    public I18N(Player player) {
        this.player = player;
        this.playerLocale = getPlayerLocale();
    }

    public List<String> translateItem(String key, Object... arguments) {
        final ArrayList<String> lines = new ArrayList<>();
        final String itemNameKey = readString(key + ".name");
        final List<String> itemLoreKey = readString(key + ".lore");
        try {
            // Item Name
            formatter.applyPattern(itemNameKey);
            final String itemNameTranslated = formatter.format(arguments);
            lines.add(itemNameTranslated);
            return lines;
        } catch (Exception e) {
            return Collections.singletonList(key);
        }
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

    public String translatePrefixless(String key, Object... arguments) {
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
            string = instance.getLocaleFileManager().getString(key);
        } else {
            final InputStream resource = instance.getResource(playerLocale.getCode() + ".yml");
            final YamlConfig yamlConfig = YamlConfig.load(resource);
            string = yamlConfig.getString(key);
        }

        return string;
    }

    private List<String> readList(String key) {
        String string;
        if (playerLocale == Locale.CUSTOM) {
            string = instance.getLocaleFileManager().getString(key);
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
