package xyz.ineanto.nicko.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.common.choice.CancelItem;
import xyz.ineanto.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.ineanto.nicko.gui.items.common.choice.ConfirmItem;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class ChoiceGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public ChoiceGUI(Player player, ChoiceCallback callback) {
        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        final ConfirmItem confirmItem = new ConfirmItem(player, callback);
        final CancelItem cancelItem = new CancelItem(player, callback);

        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.CONFIRM, false);
        this.gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ % & & & &",
                        "@ @ @ @ I & & & &",
                        "@ @ @ @ % & & & &"
                )
                .addIngredient('@', confirmItem.get())
                .addIngredient('&', cancelItem.get())
                .addIngredient('I', new SimpleItem(playerLanguage.translateItem(new ItemBuilder(Material.PAPER), LanguageKey.GUI.Choice.CHOOSE)))
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
