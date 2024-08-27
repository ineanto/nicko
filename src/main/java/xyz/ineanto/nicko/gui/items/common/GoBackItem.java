package xyz.ineanto.nicko.gui.items.common;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

public class GoBackItem {
    private final PlayerLanguage playerLanguage;

    public GoBackItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get(Gui gui, String parentTitle) {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.ARROW);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.GO_BACK);
        }, click -> {
            click.getEvent().getView().close();
            Window.single().setGui(gui).setTitle(parentTitle).open(click.getPlayer());
            return true;
        });
    }
}