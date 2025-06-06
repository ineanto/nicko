package xyz.ineanto.nicko.gui;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class FavoritesGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public FavoritesGUI(Player player) {
        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.FAVORITES, false);

        final HomeGUI parent = new HomeGUI(player);
        final GoBackItem backItem = new GoBackItem(player);

        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S C E # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
                .build();
        this.player = player;
    }

    public Gui getGUI() {
        return gui;
    }

    public String getTitle() {
        return title;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
