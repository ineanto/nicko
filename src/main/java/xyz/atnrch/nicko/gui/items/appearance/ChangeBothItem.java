package xyz.atnrch.nicko.gui.items.appearance;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeBothItem extends SuppliedItem {
    public ChangeBothItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.END_PORTAL_FRAME);
            builder.setDisplayName("§6Skin §fand §6name §fchange");
            builder.addLoreLines("§7Change both your skin and name.");
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
