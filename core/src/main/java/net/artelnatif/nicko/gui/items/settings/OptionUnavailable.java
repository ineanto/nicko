package net.artelnatif.nicko.gui.items.settings;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class OptionUnavailable extends SuppliedItem {
    public OptionUnavailable() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.RED_TERRACOTTA);
            builder.setDisplayName("§cOption unavailable :(");
            builder.addLoreLines("§7This option is disabled due to the",
                    "§7feature it controls being disabled.");
            return builder;
        }, click -> true);
    }
}
