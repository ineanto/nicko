package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

public class GoBackItem extends SuppliedItem {
    public GoBackItem(Gui gui, String parentTitle) {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ARROW);
            builder.setDisplayName("Go back");
            builder.addLoreLines("§7Return to the previous window.");
            return builder;
        }, click -> {
            click.getEvent().getView().close();
            Window.single().setGui(gui).setTitle(parentTitle).open(click.getPlayer());
            return true;
        });
    }
}
