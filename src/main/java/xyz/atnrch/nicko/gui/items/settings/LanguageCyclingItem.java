package xyz.atnrch.nicko.gui.items.settings;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.NickoProfile;
import xyz.atnrch.nicko.i18n.Locale;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
    private final ItemProvider[] providers = getItems();

    public AbstractItem get(Player player) {
        final Optional<NickoProfile> profile = NickoBukkit.getInstance().getDataStore().getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int localeOrdinal = nickoProfile.getLocale().ordinal();
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                nickoProfile.setLocale(Locale.values()[integer]);
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
            }, localeOrdinal, providers);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider generateItem(Locale locale, List<Locale> locales) {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
        builder.setDisplayName("§fLanguage");
        for (Locale value : locales) {
            if (locale != value) {
                builder.addLoreLines("§7> " + value.getName());
            } else {
                builder.addLoreLines("§6§l> §f" + value.getName());
            }
        }
        builder.addLoreLines("§7§oCycle through the values by", "§7§oleft and right clicking.");
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