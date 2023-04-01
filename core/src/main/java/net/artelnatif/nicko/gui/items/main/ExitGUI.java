package net.artelnatif.nicko.gui.items.main;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class ExitGUI extends SimpleItem {
    public ExitGUI() {
        super(new ItemBuilder(Material.OAK_DOOR).setDisplayName("§fExit"), click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
            }
        });
    }
}
