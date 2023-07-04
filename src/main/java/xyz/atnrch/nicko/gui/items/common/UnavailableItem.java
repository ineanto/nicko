package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class UnavailableItem extends SuppliedItem {
    public UnavailableItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.RED_TERRACOTTA);
            builder.setDisplayName("§fUnavailable");
            builder.addLoreLines("§7§oThis button is disabled.");
            return builder;
        }, click -> true);
    }
}
