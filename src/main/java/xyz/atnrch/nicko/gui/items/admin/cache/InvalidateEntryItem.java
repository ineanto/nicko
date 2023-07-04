package xyz.atnrch.nicko.gui.items.admin.cache;

import xyz.atnrch.nicko.gui.CacheDetailedGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateEntryItem extends SuppliedItem {
    public InvalidateEntryItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAPER);
            builder.setDisplayName("§fInvalidate specific entry");
            builder.addLoreLines("§7Select a specific skin to invalidate.");
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new CacheDetailedGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
