package xyz.atnrch.nicko.gui.items.appearance;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeNameItem extends SuppliedItem {
    public ChangeNameItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG);
            builder.setDisplayName("Change §6name");
            builder.addLoreLines("§7Will open a GUI to change your name only.");
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
