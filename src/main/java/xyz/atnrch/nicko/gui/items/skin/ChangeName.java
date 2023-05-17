package xyz.atnrch.nicko.gui.items.skin;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeName extends SuppliedItem {
    public ChangeName() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG);
            builder.setDisplayName("§fChange §6name");
            builder.addLoreLines("§7Only change your name.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final AnvilManager manager = new AnvilManager(click.getPlayer());
                manager.openNameAnvil();
            }
            return true;
        });
    }
}
