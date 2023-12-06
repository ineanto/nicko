package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.AdminGUI;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class AdminAccessItem {
    private final I18N i18n;

    public AdminAccessItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMMAND_BLOCK_MINECART);
            return i18n.translateItem(builder, I18NDict.GUI.Home.ADMIN);
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new AdminGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
