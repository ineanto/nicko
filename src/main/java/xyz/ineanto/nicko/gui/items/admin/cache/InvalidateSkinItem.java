package xyz.ineanto.nicko.gui.items.admin.cache;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.InvalidateSkinGUI;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateSkinItem {
    private final I18N i18n;

    public InvalidateSkinItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAPER);
            return i18n.translateItem(builder, I18NDict.GUI.Admin.Cache.INVALIDATE_SKIN);
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new InvalidateSkinGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
