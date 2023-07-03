package xyz.atnrch.nicko.gui;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.appearance.ChangeBothItem;
import xyz.atnrch.nicko.gui.items.appearance.ChangeNameItem;
import xyz.atnrch.nicko.gui.items.appearance.ChangeSkinItem;
import xyz.atnrch.nicko.gui.items.home.AdminAccessItem;
import xyz.atnrch.nicko.gui.items.home.ExitItem;
import xyz.atnrch.nicko.gui.items.home.ResetItem;
import xyz.atnrch.nicko.gui.items.home.SettingsAccessItem;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class HomeGUI {
    private final String title = "Nicko - Home";
    private final Player player;
    private final Gui gui;

    public HomeGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "A # # N B S # # #",
                "E P # # # # # # R"};

        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            dynamicStructure[2] = dynamicStructure[2].replace("A", "#");
        }

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('E', new ExitItem())
                .addIngredient('R', new ResetItem())
                .addIngredient('N', new ChangeNameItem())
                .addIngredient('B', new ChangeBothItem())
                .addIngredient('S', new ChangeSkinItem(player))
                .addIngredient('P', new SettingsAccessItem())
                .addIngredient('A', new AdminAccessItem())
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
