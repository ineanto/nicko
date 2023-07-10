package xyz.atnrch.nicko.gui.items.home;

import xyz.atnrch.nicko.gui.SettingsGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class SettingsAccessItem extends SuppliedItem {
    public SettingsAccessItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMPARATOR);
            builder.setDisplayName("Settings");
            builder.addLoreLines("§7Configure your experience.");
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
