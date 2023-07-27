package xyz.atnrch.nicko.gui;

import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import xyz.atnrch.nicko.gui.items.settings.BungeeCordCyclingItem;
import xyz.atnrch.nicko.gui.items.settings.LanguageCyclingItem;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class SettingsGUI {
    public static final String TITLE = "Nicko > Settings";

    private final Player player;
    private final Gui gui;

    public SettingsGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# # # L T U # # #",
                "B # # # # # # # #"
        };

        // TODO: 3/6/23 Replace when Redis is not enabled
        dynamicStructure[1] = dynamicStructure[1].replace("T", "U");

        final HomeGUI parent = new HomeGUI(player);
        final LanguageCyclingItem languageItem = new LanguageCyclingItem(player);
        final BungeeCordCyclingItem bungeeCordItem = new BungeeCordCyclingItem(player);
        final GoBackItem backItem = new GoBackItem(player);

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
                .addIngredient('L', languageItem.get())
                .addIngredient('T', bungeeCordItem.get())
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(TITLE).open(player);
    }
}
