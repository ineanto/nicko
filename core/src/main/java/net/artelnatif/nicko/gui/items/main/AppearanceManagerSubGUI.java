package net.artelnatif.nicko.gui.items.main;

import net.artelnatif.nicko.gui.AppearanceManagerGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class AppearanceManagerSubGUI extends SuppliedItem {
    public AppearanceManagerSubGUI() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ENDER_EYE);
            builder.setDisplayName("§fManage §6appearance§f...");
            builder.addLoreLines("§7Access the appearance manager.");
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new AppearanceManagerGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
