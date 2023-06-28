package xyz.atnrch.nicko.gui;

import xyz.atnrch.nicko.gui.items.main.AdminSubGUI;
import xyz.atnrch.nicko.gui.items.main.ResetAppearance;
import xyz.atnrch.nicko.gui.items.main.SettingsSubGUI;
import xyz.atnrch.nicko.gui.items.skin.ChangeName;
import xyz.atnrch.nicko.gui.items.skin.ChangeNameAndSkin;
import xyz.atnrch.nicko.gui.items.skin.ChangeSkin;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class MainGUI {
    private final String title = "Nicko - Home";
    private final Player player;
    private final Gui gui;

    public MainGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "A # # N B S # # #",
                "E P # # # # # # R"};

        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            dynamicStructure[2] = dynamicStructure[2].replace("A", "#");
        }

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('R', new ResetAppearance())
                .addIngredient('N', new ChangeName())
                .addIngredient('B', new ChangeNameAndSkin())
                .addIngredient('S', new ChangeSkin(player))
                .addIngredient('P', new SettingsSubGUI())
                .addIngredient('A', new AdminSubGUI())
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
