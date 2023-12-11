package xyz.ineanto.nicko.i18n;

import com.github.jsixface.YamlConfig;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I18N {
    private final MessageFormat formatter = new MessageFormat("");
    private final Logger logger = Logger.getLogger("I18N");
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final Pattern replacementPattern = Pattern.compile("\\{\\d+}$", Pattern.DOTALL);
    private final YamlConfig yamlConfig;
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

    public AbstractItemBuilder<?> translateItem(AbstractItemBuilder<?> item, String key, Object... args) {
        final ItemTranslation translation = fetchTranslation(key, args);
        item.setDisplayName(translation.getName());
        translation.getLore().forEach(item::addLoreLines);
        return item;
    }

    public ItemTranslation fetchTranslation(String key, Object... args) {
        final String nameKey = key + ".name";
        final String loreKey = key + ".lore";
        final String name = readString(nameKey);
        final ArrayList<String> lore = readList(loreKey);

        if (name == null) {
            logger.warning(nameKey + " doesn't exists! Please translate this entry.");
            return new ItemTranslation(nameKey, new ArrayList<String>() {{
                add(loreKey);
            }});
        }

        // Add all elements to a list
        final ArrayList<String> toTranslate = new ArrayList<>();
        toTranslate.add(name);
        if (lore != null && !lore.isEmpty()) {
            toTranslate.addAll(lore);
        }

        // Set starting index to 0
        int lineIndex = 0;
        int replacementIndex = 0;

        // While iterator next value exists/isn't null
        final Iterator<String> iterator = toTranslate.iterator();
        while (iterator.hasNext() && iterator.next() != null) {
            // Get the current line
            final String currentLine = toTranslate.get(lineIndex);

            // If the line doesn't contain {i}, skip it
            final Matcher matcher = replacementPattern.matcher(currentLine);
            if (!matcher.find()) {
                lineIndex++;
                continue;
            }

            // If it does, replace the content with the args at position replacementIndex
            if (replacementIndex < args.length && args[replacementIndex] != null) {
                // Replace with the corresponding varargs index
                toTranslate.set(lineIndex, currentLine.replace("{" + replacementIndex + "}", args[replacementIndex].toString()));
                replacementIndex++;
            }

            // Increment the index
            lineIndex++;
        }

        if (lore == null || lore.isEmpty()) {
            return new ItemTranslation(toTranslate.get(0), new ArrayList<>());
        }
        return new ItemTranslation(toTranslate.get(0), new ArrayList<>(toTranslate.subList(1, toTranslate.size())));
    }

    public String translate(String key, Object... arguments) {
        final String translation = readString(key);
        try {
            formatter.applyPattern(translation);
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

    public Locale getPlayerLocale() {
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
        if (optionalProfile.isPresent()) {
            return optionalProfile.get().getLocale();
        } else {
            return Locale.FALLBACK_LOCALE;
        }
    }
}
