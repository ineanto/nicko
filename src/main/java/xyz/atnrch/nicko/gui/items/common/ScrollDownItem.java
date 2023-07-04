package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem;

public class ScrollDownItem extends ScrollItem {

    public ScrollDownItem() {
        super(1);
    }

    @Override
    public ItemProvider getItemProvider(ScrollGui gui) {
        ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        builder.setDisplayName("§fScroll down");
        if (!gui.canScroll(1))
            builder.addLoreLines("§7§o(You can't scroll further down.)");

        return builder;
    }
}

