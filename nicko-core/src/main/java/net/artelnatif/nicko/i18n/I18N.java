package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Optional;

public class I18N {
    private final static MessageFormat formatter = new MessageFormat("");
    private static final Yaml yaml = new Yaml();

    private static Locale getLocale(Player player) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        try {
            final Optional<NickoProfile> profile = instance.getDataStore().getData(player.getUniqueId());
            return profile.isEmpty() ? Locale.FALLBACK_LOCALE : profile.get().getLocale();
        } catch (IllegalArgumentException exception) {
            instance.getLogger().severe("Invalid locale provided by " + player.getName() + ", defaulting to " + Locale.FALLBACK_LOCALE.getCode() + ".");
            return Locale.FALLBACK_LOCALE;
        }
    }

    public static String translate(Player player, I18NDict key, Object... arguments) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final String translation = findTranslation(player, key);

        try {
            formatter.applyPattern(translation);
            return instance.getNickoConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return instance.getNickoConfig().getPrefix() + key.key();
        }
    }

    public static String translateWithoutPrefix(Player player, I18NDict key, Object... arguments) {
        final String translation = findTranslation(player, key);
        try {
            formatter.applyPattern(translation);
            return formatter.format(arguments);
        } catch (Exception e) {
            return key.key();
        }
    }

    private static String findTranslation(Player player, I18NDict key) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final Locale locale = getLocale(player);
        String translation;
        if (locale == Locale.CUSTOM) {
            translation = instance.getLocaleFileManager().getFromFile(key.key());
        } else {
            final HashMap<String, String> values = yaml.load(instance.getResource(locale.getCode() + ".yml"));
            translation = values.getOrDefault(key.key(), key.key());
        }

        return translation;
    }
}
