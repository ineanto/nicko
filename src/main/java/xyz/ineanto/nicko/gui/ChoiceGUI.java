package xyz.ineanto.nicko.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.common.choice.CancelItem;
import xyz.ineanto.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.ineanto.nicko.gui.items.common.choice.ConfirmItem;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class ChoiceGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public ChoiceGUI(Player player, ChoiceCallback callback) {
        final I18N i18n = new I18N(player);
        final ConfirmItem confirmItem = new ConfirmItem(player, callback);
        final CancelItem cancelItem = new CancelItem(player, callback);

        this.title = i18n.translate(I18NDict.GUI.Titles.CONFIRM, false);
        this.gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ % & & & &",
                        "@ @ @ @ I & & & &",
                        "@ @ @ @ % & & & &"
                )
                .addIngredient('@', confirmItem.get())
                .addIngredient('&', cancelItem.get())
                .addIngredient('I', new SimpleItem(i18n.translateItem(new ItemBuilder(Material.PAPER), I18NDict.GUI.Choice.CHOOSE)))
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
