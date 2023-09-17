package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
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
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.GO_BACK);
            builder.setDisplayName(translation.getName());
            return builder;
        }, click -> {
            click.getEvent().getView().close();
            Window.single().setGui(gui).setTitle(parentTitle).open(click.getPlayer());
            return true;
        });
    }
}