package net.artelnatif.nicko.bukkit.gui.items.settings;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import de.studiocode.invui.item.impl.CycleItem;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.bukkit.i18n.Locale;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class LanguageCycling {
    private final ItemProvider[] providers = getItems();

    public BaseItem get(Player player) {
        final NickoBukkit instance = NickoBukkit.getInstance();
        Optional<NickoProfile> profile = instance.getNicko().getDataStore().getData(player.getUniqueId());
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
        builder.setDisplayName("§6Select your language:");
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
        if (!instance.getNicko().getConfig().isCustomLocale()) {
            localesToGenerate.remove(Locale.CUSTOM);
        }
        localesToGenerate.forEach(locale -> items.add(generateItem(locale, localesToGenerate)));
        return items.toArray(new ItemProvider[]{});
    }
}