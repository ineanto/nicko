package xyz.ineanto.nicko.gui;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.gui.items.settings.LanguageCyclingItem;
import xyz.ineanto.nicko.gui.items.settings.RandomSkinCyclingItem;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class SettingsGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public SettingsGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# # # L # R # # #",
                "B # # # # # # # #"
        };

        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.SETTINGS, false);

        final HomeGUI parent = new HomeGUI(player);
        final LanguageCyclingItem languageItem = new LanguageCyclingItem(player);
        final RandomSkinCyclingItem skinItem = new RandomSkinCyclingItem(player);
        final GoBackItem backItem = new GoBackItem(player);

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
                .addIngredient('L', languageItem.get())
                .addIngredient('R', skinItem.get())
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
