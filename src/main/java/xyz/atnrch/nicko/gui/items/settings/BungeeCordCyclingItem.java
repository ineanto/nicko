package xyz.atnrch.nicko.gui.items.settings;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.NickoProfile;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.CycleItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Optional;

public class BungeeCordCyclingItem {
    private final ItemProvider[] providers = new ItemProvider[]{
            getItemProviderForValue(true),
            getItemProviderForValue(false)
    };

    public AbstractItem get(Player player) {
        final Optional<NickoProfile> profile = NickoBukkit.getInstance().getDataStore().getData(player.getUniqueId());
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
        builder.setDisplayName("§fBungeeCord transfer");
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
