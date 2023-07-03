package xyz.atnrch.nicko.gui.items.common.confirm;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ConfirmItem extends SuppliedItem {
    public ConfirmItem(ChoiceCallback callback) {
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
