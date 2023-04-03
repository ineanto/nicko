package net.artelnatif.nicko.gui.items.admin.cache;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class CacheInvalidate extends SuppliedItem {
    public CacheInvalidate() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            builder.setDisplayName("§fInvalidate §6skin cache");
            builder.addLoreLines(
                    "§c§oNOT RECOMMENDED",
                    "§7Invalidates every skin entry present in the cache.",
                    "§7Does not reset player disguises.",
                    "§7Could be useful if a skin has been updated",
                    "§7recently and the cache is now outdated.");
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final Player player = click.getPlayer();
                click.getEvent().getView().close();
                player.sendMessage(I18N.translate(player, I18NDict.Event.Admin.CACHE_CLEAN));
                NickoBukkit.getInstance().getMojangAPI().getCache().invalidateAll();
                return true;
            }
            return false;
        });
    }
}
