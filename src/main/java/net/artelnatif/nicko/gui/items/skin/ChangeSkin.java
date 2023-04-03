package net.artelnatif.nicko.gui.items.skin;

import net.artelnatif.nicko.anvil.AnvilManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeSkin extends SuppliedItem {
    public ChangeSkin(Player player) {
        super(() -> {
            final SkullBuilder builder = new SkullBuilder(player.getName());
            builder.setDisplayName("§fChange §6skin");
            builder.addLoreLines("§7Only change your skin.");
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
