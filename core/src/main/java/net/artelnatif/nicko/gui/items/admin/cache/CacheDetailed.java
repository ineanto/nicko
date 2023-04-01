package net.artelnatif.nicko.gui.items.admin.cache;

import net.artelnatif.nicko.gui.admin.cache.CacheDetailledGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class CacheDetailed extends SuppliedItem {
    public CacheDetailed() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAPER);
            builder.setDisplayName("§6Invalidate specific skin...");
            builder.addLoreLines("§7Select a specific skin to invalidate.");
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new CacheDetailledGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
