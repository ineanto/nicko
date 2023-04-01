package net.artelnatif.nicko.gui.items.main;

import net.artelnatif.nicko.gui.SettingsGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class SettingsSubGUI extends SuppliedItem {
    public SettingsSubGUI() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMPARATOR);
            builder.setDisplayName("§fSettings...");
            builder.addLoreLines("§7Adjust your preferences.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new SettingsGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
