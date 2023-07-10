package xyz.atnrch.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.gui.AdminGUI;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class AdminAccessItem extends SuppliedItem {
    public AdminAccessItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMMAND_BLOCK_MINECART);
            builder.setDisplayName("Administration panel");
            builder.addLoreLines("§7Configure and manage Nicko.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new AdminGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
