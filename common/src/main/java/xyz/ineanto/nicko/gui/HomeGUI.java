package xyz.ineanto.nicko.gui;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.appearance.ChangeBothItem;
import xyz.ineanto.nicko.gui.items.appearance.ChangeNameItem;
import xyz.ineanto.nicko.gui.items.appearance.ChangeSkinItem;
import xyz.ineanto.nicko.gui.items.home.*;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class HomeGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public HomeGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # D # # # #",
                "A # # N B S # # #",
                "E P # # # # # # R"};

        if (!player.isOp() || !player.hasPermission("nicko.admin")) {
            dynamicStructure[2] = dynamicStructure[2].replace("A", "#");
        }

        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.HOME, false);

        final ExitItem exitItem = new ExitItem(player);
        final ResetItem resetItem = new ResetItem(player);
        final ChangeNameItem changeNameItem = new ChangeNameItem(player);
        final ChangeBothItem changeBothItem = new ChangeBothItem(player);
        final ChangeSkinItem changeSkinItem = new ChangeSkinItem(player);
        final SettingsAccessItem settingsAccessItem = new SettingsAccessItem(player);
        final AdminAccessItem adminAccessItem = new AdminAccessItem(player);
        final RandomSkinItem randomSkinItem = new RandomSkinItem(player);

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('E', exitItem.get())
                .addIngredient('R', resetItem.get())
                .addIngredient('N', changeNameItem.get())
                .addIngredient('B', changeBothItem.get())
                .addIngredient('S', changeSkinItem.get())
                .addIngredient('P', settingsAccessItem.get())
                .addIngredient('A', adminAccessItem.get())
                .addIngredient('D', randomSkinItem.get())
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
