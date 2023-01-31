package net.artelnatif.nicko.bukkit.gui.items.settings;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import de.studiocode.invui.item.impl.CycleItem;
import de.studiocode.invui.item.impl.SimpleItem;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class BungeeCordCycling {
    private final ItemProvider[] providers = new ItemProvider[]{
            getItemProviderForValue(true),
            getItemProviderForValue(false)
    };

    public BaseItem get(Player player) {
        Optional<NickoProfile> profile = NickoBukkit.getInstance().getNicko().getDataStore().getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int startingState = nickoProfile.isBungeecordTransfer() ? 0 : 1;
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                nickoProfile.setBungeecordTransfer(integer != 1);
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
            }, startingState, providers);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider getItemProviderForValue(boolean enabled) {
        final ItemBuilder builder = new ItemBuilder(Material.COMPASS);
        builder.setDisplayName("§6BungeeCord transfer:");
        if (enabled) {
            builder.addLoreLines("§7> §cDisabled");
            builder.addLoreLines("§6§l> §a§lEnabled");
        } else {
            builder.addLoreLines("§6§l> §c§lDisabled");
            builder.addLoreLines("§7> §aEnabled");
        }
        builder.addLoreLines("§7§oCycle through the values by", "§7§oleft and right clicking.");
        return builder;
    }
}
