package xyz.ineanto.nicko.language;

import com.github.jsixface.YamlConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLanguage {
    private final MessageFormat formatter = new MessageFormat("");
    private final Nicko instance = Nicko.getInstance();
    private final Pattern replacementPattern = Pattern.compile("(?ms)\\{\\d+}");
    private final YamlConfig yamlConfig;
    private final Language playerLanguage;

    public PlayerLanguage(Player player) {
        final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
        this.playerLanguage = optionalProfile.map(NickoProfile::getLocale).orElse(Language.ENGLISH);
        this.yamlConfig = getYamlConfig();
    }

    public PlayerLanguage(Language language) {
        this.playerLanguage = language;
        this.yamlConfig = getYamlConfig();
    }

    public AbstractItemBuilder<?> translateItem(AbstractItemBuilder<?> item, String key, Object... args) {
        final Translation translation = translateAndReplace(key, args);

        // Name serialization
        final Component deserializedName = MiniMessage.miniMessage().deserialize(translation.name());
        final String serializedName = LegacyComponentSerializer.legacySection().serialize(deserializedName);

        // Lore serialization
        translation.lore().replaceAll(s -> {
            final Component deserializedLoreLine = MiniMessage.miniMessage().deserialize(s);
            return LegacyComponentSerializer.legacySection().serialize(deserializedLoreLine);
        });

        item.setDisplayName(serializedName);
        translation.lore().forEach(item::addLoreLines);
        return item;
    }

    public Translation translateAndReplace(String key, Object... args) {
        final String nameKey = key + ".name";
        final String loreKey = key + ".lore";
        final String name = readString(nameKey);
        final ArrayList<String> lore = readList(loreKey);

        if (name == null && lore == null) {
            Nicko.getInstance().getLogger().warning(nameKey + " doesn't exists! Is your language file outdated?");
            return new Translation(nameKey, new ArrayList<>(List.of(loreKey)));
        }

        // Add all elements to a list
        final ArrayList<String> toTranslate = new ArrayList<>();
        if (name != null) {
            toTranslate.add(name);
        }
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
                // Replace it with the corresponding varargs index
                toTranslate.set(lineIndex, currentLine.replace("{" + replacementIndex + "}", args[replacementIndex].toString()));
                replacementIndex++;
            }

            // Increment the index
            lineIndex++;
        }

        if (name == null && !lore.isEmpty()) {
            // Empty name, valid lore
            return new Translation(null, toTranslate);
        } else if (name != null && (lore == null || lore.isEmpty())) {
            // Valid name, empty lore
            return new Translation(toTranslate.getFirst(), new ArrayList<>(Collections.emptyList()));
        } else {
            // Valid name, valid lore
            return new Translation(toTranslate.getFirst(), new ArrayList<>(toTranslate.subList(1, toTranslate.size())));
        }
    }

    public String translate(String key, boolean prefix, Object... arguments) {
        final String translation = readStringWithMiniMessage(key);

        try {
            formatter.applyPattern(translation);
            return (prefix ? getPrefix() + " " : "") + formatter.format(arguments);
        } catch (Exception e) {
            return (prefix ? getPrefix() + " " : "") + key;
        }
    }

    public String translateWithWhoosh(String key, Object... arguments) {
        final String translation = readStringWithMiniMessage(key);

        try {
            formatter.applyPattern(translation);
            return getWhoosh() + " " + formatter.format(arguments);
        } catch (Exception e) {
            return getWhoosh() + " " + key;
        }
    }

    public String translateWithOops(String key, Object... arguments) {
        final String translation = readStringWithMiniMessage(key);

        try {
            formatter.applyPattern(translation);
            return getOops() + " " + formatter.format(arguments);
        } catch (Exception e) {
            return getOops() + " " + key;
        }
    }

    public Component getPrefixComponent() {
        return MiniMessage.miniMessage().deserialize(readString(LanguageKey.PREFIX));
    }

    private String readString(String key) {
        return yamlConfig.getString(key);
    }

    private String getPrefix() {
        return LegacyComponentSerializer.legacySection().serialize(getPrefixComponent());
    }

    private String readStringWithMiniMessage(String key) {
        return LegacyComponentSerializer.legacySection().serialize(MiniMessage.miniMessage().deserialize(readString(key)));
    }

    private ArrayList<String> readList(String key) {
        return yamlConfig.getStringList(key);
    }

    private String getWhoosh() {
        return readStringWithMiniMessage(LanguageKey.WHOOSH);
    }

    private String getOops() {
        return readStringWithMiniMessage(LanguageKey.OOPS);
    }

    private YamlConfig getYamlConfig() {
        if (playerLanguage == Language.CUSTOM) {
            return instance.getCustomLocale().getYamlFile();
        } else {
            final InputStream resource = instance.getResource(playerLanguage.getCode() + ".yml");
            return new YamlConfig(resource);
        }
    }
}
