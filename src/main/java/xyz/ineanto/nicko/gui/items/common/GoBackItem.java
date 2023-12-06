package xyz.ineanto.nicko.gui.items.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

public class GoBackItem {
    private final I18N i18n;

    public GoBackItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get(Gui gui, String parentTitle) {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ARROW);
            return i18n.translateItem(builder, I18NDict.GUI.GO_BACK);
        }, click -> {
            click.getEvent().getView().close();
            Window.single().setGui(gui).setTitle(parentTitle).open(click.getPlayer());
            return true;
        });
    }
}