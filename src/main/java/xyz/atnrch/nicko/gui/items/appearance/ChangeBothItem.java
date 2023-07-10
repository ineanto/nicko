package xyz.atnrch.nicko.gui.items.appearance;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeBothItem extends SuppliedItem {
    public ChangeBothItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TOTEM_OF_UNDYING);
            builder.setDisplayName("§6Skin §fand §6name §fchange");
            builder.addLoreLines("§7Will open a GUI to change both your name and your skin.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final AnvilManager manager = new AnvilManager(click.getPlayer());
                manager.openNameThenSkinAnvil();
            }
            return true;
        });
    }
}
