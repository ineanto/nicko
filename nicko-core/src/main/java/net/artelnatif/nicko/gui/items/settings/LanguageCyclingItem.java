package net.artelnatif.nicko.gui.items.settings;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import de.studiocode.invui.item.impl.CycleItem;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.Locale;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class LanguageCyclingItem {
    private final ItemProvider[] possibleLocales = new ItemProvider[]{
            getItemProviderForLocale(Locale.ENGLISH),
            getItemProviderForLocale(Locale.FRENCH),
            getItemProviderForLocale(Locale.CUSTOM),
    };

    public BaseItem get(Player player) {
        Optional<NickoProfile> profile = NickoBukkit.getInstance().getDataStore().getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int localeOrdinal = nickoProfile.getLocale().ordinal();
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                nickoProfile.setLocale(Locale.values()[integer]);
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
            }, localeOrdinal, possibleLocales);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider getItemProviderForLocale(Locale locale) {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
        builder.setDisplayName("§6Select your language:");
        for (Locale value : Locale.values()) {
            if (locale != value) {
                builder.addLoreLines("§7> " + value.getName());
            } else {
                builder.addLoreLines("§6§l> §f" + value.getName());
            }
        }
        builder.addLoreLines("§7§oCycle through the values by", "§7§oleft and right clicking.");
        return builder;
    }
}