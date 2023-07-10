package xyz.atnrch.nicko.gui.items.home;

import com.comphenix.protocol.utility.MinecraftVersion;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;

public class ExitItem extends SimpleItem {
    public ExitItem() {
        super(new ItemBuilder(Material.OAK_DOOR).setDisplayName("Exit"), click -> {
            if(MinecraftVersion.BEE_UPDATE.atOrAbove()) {

            }
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
            }
        });
    }
}
