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
    private final Player player;
    private final I18N i18n;

    public GoBackItem(Player player) {
        this.player = player;
        this.i18n = new I18N(player);
    }

    public SuppliedItem get(Gui gui, String parentTitle) {
        final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.GO_BACK);
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ARROW);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, click -> {
            click.getEvent().getView().close();
            Window.single().setGui(gui).setTitle(parentTitle).open(click.getPlayer());
            return true;
        });
    }
}