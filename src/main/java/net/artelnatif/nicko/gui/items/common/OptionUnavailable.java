package net.artelnatif.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class OptionUnavailable extends SuppliedItem {
    public OptionUnavailable() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.RED_TERRACOTTA);
            builder.setDisplayName("§cFeature unavailable :(");
            builder.addLoreLines("§7This button is disabled.");
            return builder;
        }, click -> true);
    }
}
