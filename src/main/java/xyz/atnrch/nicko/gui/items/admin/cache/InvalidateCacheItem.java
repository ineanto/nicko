package xyz.atnrch.nicko.gui.items.admin.cache;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateCacheItem extends SuppliedItem {
    public InvalidateCacheItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            builder.setDisplayName("Invalidate cache");
            builder.addLoreLines(
                    "§c§oNOT RECOMMENDED",
                    "§7Invalidates every skin entry present in the cache.",
                    "§7Does not reset player disguises.");
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();

                final Player player = click.getPlayer();
                final I18N i18n = new I18N(player);
                player.sendMessage(i18n.translate(I18NDict.Event.Admin.Cache.INVALIDATE_ALL));
                NickoBukkit.getInstance().getMojangAPI().getSkinCache().invalidateAll();
                return true;
            }
            return false;
        });
    }
}
