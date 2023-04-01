package net.artelnatif.nicko.gui.items.skin;

import net.artelnatif.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeNameAndSkin extends SuppliedItem {
    public ChangeNameAndSkin() {
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
