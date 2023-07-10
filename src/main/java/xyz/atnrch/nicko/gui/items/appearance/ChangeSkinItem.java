package xyz.atnrch.nicko.gui.items.appearance;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeSkinItem extends SuppliedItem {
    public ChangeSkinItem(Player player) {
        super(() -> {
            final SkullBuilder builder = new SkullBuilder(player.getName());
            builder.setDisplayName("§fChange §6skin");
            builder.addLoreLines("§7Will open a GUI to change your skin only.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final AnvilManager manager = new AnvilManager(click.getPlayer());
                manager.openSkinAnvil();
            }
            return true;
        });
    }
}