package net.artelnatif.nicko.bukkit.gui.items.common;

import de.studiocode.invui.gui.impl.ScrollGUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.controlitem.ScrollItem;
import org.bukkit.Material;

public class ScrollUp extends ScrollItem {

    public ScrollUp() {
        super(-1);
    }

    @Override
    public ItemProvider getItemProvider(ScrollGUI gui) {
        ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        builder.setDisplayName("§7Scroll up");
        if (!gui.canScroll(-1))
            builder.addLoreLines("§cYou've reached the top");

        return builder;
    }

}

