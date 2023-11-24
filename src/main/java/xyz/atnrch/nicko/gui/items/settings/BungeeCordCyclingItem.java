package xyz.atnrch.nicko.gui.items.settings;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.CycleItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Optional;

public class BungeeCordCyclingItem {
    private final Player player;
    private final I18N i18n;
    private final ItemProvider[] providers;

    public BungeeCordCyclingItem(Player player) {
        this.player = player;
        this.i18n = new I18N(player);
        this.providers = new ItemProvider[]{
                getItemProviderForValue(true),
                getItemProviderForValue(false)
        };
    }

    public AbstractItem get() {
        final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
        final Optional<NickoProfile> profile = dataStore.getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int startingState = nickoProfile.isBungeecordTransfer() ? 0 : 1;
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                nickoProfile.setBungeecordTransfer(integer != 1);
                dataStore.updateCache(player.getUniqueId(), nickoProfile);
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
            }, startingState, providers);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider getItemProviderForValue(boolean ignored) {
        final ItemBuilder builder = new ItemBuilder(Material.COMPASS);
        final ItemTranslation translation = i18n.fetchTranslation(I18NDict.GUI.Settings.BUNGEECORD);

        builder.setDisplayName(translation.getName());
        translation.getLore().forEach(builder::addLoreLines);
        /*
         if (enabled) {
         builder.addLoreLines("§7> §cDisabled");
         builder.addLoreLines("§6§l> §a§lEnabled");
         } else {
         builder.addLoreLines("§6§l> §c§lDisabled");
         builder.addLoreLines("§7> §aEnabled");
         }
         builder.addLoreLines("§7§oCycle through the values by", "§7§oleft and right clicking.");
         */
        return builder;
    }
}
