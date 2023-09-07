package xyz.atnrch.nicko.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.common.choice.CancelItem;
import xyz.atnrch.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.atnrch.nicko.gui.items.common.choice.ConfirmItem;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class ChoiceGUI {
    private final Player player;
    private final Gui gui;

    public ChoiceGUI(Player player, ChoiceCallback callback) {
        final I18N i18n = new I18N(player);
        final ConfirmItem confirmItem = new ConfirmItem(player, callback);
        final CancelItem cancelItem = new CancelItem(player, callback);
        final ItemTranslation chooseItemTranslation = i18n.translateItem(I18NDict.GUI.Choice.CHOOSE);

        this.gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ % & & & &",
                        "@ @ @ @ I & & & &",
                        "@ @ @ @ % & & & &"
                )
                .addIngredient('@', confirmItem.get())
                .addIngredient('&', cancelItem.get())
                .addIngredient('I', new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName(chooseItemTranslation.getName()).get()))
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle("... > Invalidate > Confirm").open(player);
    }
}
