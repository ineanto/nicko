package xyz.ineanto.nicko.gui.items.common.choice;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class CancelItem {
    private final I18N i18n;
    private final ChoiceCallback callback;

    public CancelItem(Player player, ChoiceCallback callback) {
        this.i18n = new I18N(player);
        this.callback = callback;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
            return i18n.translateItem(builder, I18NDict.GUI.Choice.CANCEL);
        }, click -> {
            click.getEvent().getView().close();
            callback.onCancel();
            return true;
        });
    }
}