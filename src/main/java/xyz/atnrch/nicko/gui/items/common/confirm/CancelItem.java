package xyz.atnrch.nicko.gui.items.common.confirm;

import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class CancelItem extends SuppliedItem {
    public CancelItem(ChoiceCallback callback) {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
            builder.setDisplayName("§cCancel");
            return builder;
        }, click -> {
            click.getEvent().getView().close();
            callback.onCancel();
            return true;
        });
    }
}