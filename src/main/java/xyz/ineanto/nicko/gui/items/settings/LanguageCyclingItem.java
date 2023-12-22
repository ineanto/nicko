package xyz.ineanto.nicko.gui.items.settings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.gui.SettingsGUI;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.i18n.Translation;
import xyz.ineanto.nicko.i18n.Locale;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.CycleItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LanguageCyclingItem {
    private final Player player;
    private final ItemProvider[] providers;
    private final I18N i18n;

    public LanguageCyclingItem(Player player) {
        this.player = player;
        this.i18n = new I18N(player);
        this.providers = getItems();
    }

    public AbstractItem get() {
        final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
        final Optional<NickoProfile> profile = dataStore.getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int localeOrdinal = nickoProfile.getLocale().ordinal();
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
                nickoProfile.setLocale(Locale.values()[integer]);
                player.getOpenInventory().close();
                if (dataStore.updateCache(player.getUniqueId(), nickoProfile).isError()) {
                    player.sendMessage(i18n.translateString(I18NDict.Event.Settings.ERROR));
                } else {
                    new SettingsGUI(player).open();
                }
            }, localeOrdinal, providers);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider generateItem(Locale locale, List<Locale> locales) {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
        final Translation translation = i18n.translate(I18NDict.GUI.Settings.LANGUAGE);
        final Translation cyclingChoicesTranslation = i18n.translate(I18NDict.GUI.Settings.CYCLING_CHOICES);

        builder.setDisplayName(Component.text(translation.name()).content());
        for (Locale value : locales) {
            if (locale != value) {
                builder.addLoreLines("§7> " + value.getName());
            } else {
                builder.addLoreLines("§6§l> §f" + value.getName());
            }
        }

        cyclingChoicesTranslation.lore().replaceAll(s -> {
            final Component deserializedLoreLine = MiniMessage.miniMessage().deserialize(s);
            return LegacyComponentSerializer.legacySection().serialize(deserializedLoreLine);
        });
        cyclingChoicesTranslation.lore().forEach(builder::addLoreLines);
        return builder;
    }

    private ItemProvider[] getItems() {
        final NickoBukkit instance = NickoBukkit.getInstance();
        final ArrayList<ItemProvider> items = new ArrayList<>();
        final ArrayList<Locale> localesToGenerate = new ArrayList<>();

        Collections.addAll(localesToGenerate, Locale.values());
        if (!instance.getNickoConfig().isCustomLocale()) {
            localesToGenerate.remove(Locale.CUSTOM);
        }
        localesToGenerate.forEach(locale -> items.add(generateItem(locale, localesToGenerate)));
        return items.toArray(new ItemProvider[]{});
    }
}