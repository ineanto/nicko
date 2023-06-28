package xyz.atnrch.nicko.gui.items.admin.cache;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateCompleteCache extends SuppliedItem {
    public InvalidateCompleteCache() {
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
                final I18N i18n = new I18N(player);
                click.getEvent().getView().close();
                player.sendMessage(i18n.translate(I18NDict.Event.Admin.CACHE_CLEAN));
                NickoBukkit.getInstance().getMojangAPI().getCache().invalidateAll();
                return true;
            }
            return false;
        });
    }
}
