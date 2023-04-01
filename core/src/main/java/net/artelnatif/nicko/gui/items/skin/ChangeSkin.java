package net.artelnatif.nicko.gui.items.skin;

import net.artelnatif.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeSkin extends SuppliedItem {
    public ChangeSkin() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ENDER_PEARL);
            builder.setDisplayName("§6Skin §fchange");
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
