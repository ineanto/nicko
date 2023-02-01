package net.artelnatif.nicko.bukkit.i18n;

import com.github.jsixface.YamlConfig;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Optional;

public class I18N {
    private final static MessageFormat formatter = new MessageFormat("");

    private static Locale getLocale(Player player) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        try {
            final Optional<NickoProfile> profile = instance.getNicko().getDataStore().getData(player.getUniqueId());
            return !profile.isPresent() ? Locale.FALLBACK_LOCALE : profile.get().getLocale();
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
            return instance.getNicko().getConfig().getPrefix() + formatter.format(arguments);
        } catch (Exception e) {
            return instance.getNicko().getConfig().getPrefix() + key.key();
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
            translation = instance.getLocaleFileManager().get(key.key());
        } else {
            final InputStream resource = instance.getResource(locale.getCode() + ".yml");
            final YamlConfig yamlConfig = YamlConfig.load(resource);
            translation = yamlConfig.getString(key.key());
        }

        return translation;
    }
}
