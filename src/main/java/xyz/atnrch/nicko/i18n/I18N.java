package xyz.atnrch.nicko.i18n;

import com.github.jsixface.YamlConfig;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.AppearanceManager;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class I18N {
    private final MessageFormat formatter = new MessageFormat("");
    private final YamlConfig yamlConfig;
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final Player player;
    private final Locale playerLocale;

    public I18N(Player player) {
        this.player = player;
        this.playerLocale = getPlayerLocale();
        this.yamlConfig = getYamlConfig();
    }

    public I18N(Locale locale) {
        this.player = null;
        this.playerLocale = locale;
        this.yamlConfig = getYamlConfig();
    }

    public ItemTranslation translateItem(String key, String... args) {
        final String name = readString(key + ".name");
        final ArrayList<String> lore = readList(key + ".lore");

        // Add all elements to a list
        final ArrayList<String> toTranslate = new ArrayList<>();
        toTranslate.add(name);
        toTranslate.addAll(lore);

        // Set starting index to 0
        int index = 0;

        // While iterator next value exists/isn't null
        final Iterator<String> iterator = toTranslate.iterator();
        while (!iterator.hasNext() || iterator.next() == null) {
            // Get the current line
            final String currentLine = toTranslate.get(index);

            // Replace with the corresponding varargs index
            toTranslate.set(index, currentLine.replace("{" + index + "}", args[index]));

            // Increment the index
            index++;
        }
        return new ItemTranslation(toTranslate.get(0), toTranslate.subList(1, toTranslate.size()));
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
        return yamlConfig.getString(key);
    }

    private ArrayList<String> readList(String key) {
        return yamlConfig.getStringList(key);
    }

    private YamlConfig getYamlConfig() {
        if (playerLocale == Locale.CUSTOM) {
            return instance.getLocaleFileManager().getYamlFile();
        } else {
            final InputStream resource = instance.getResource(playerLocale.getCode() + ".yml");
            return new YamlConfig(resource);
        }
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
