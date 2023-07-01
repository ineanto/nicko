package xyz.atnrch.nicko.gui.items.confirm;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class Confirm extends SuppliedItem {
    public Confirm(ActionCallback callback) {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
            builder.setDisplayName("§aConfirm");
            return builder;
        }, click -> {
            click.getEvent().getView().close();
            callback.onConfirm();
            return true;
        });
    }
}
