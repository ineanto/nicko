package net.artelnatif.nicko.gui;

import net.artelnatif.nicko.gui.items.main.AdminSubGUI;
import net.artelnatif.nicko.gui.items.main.ResetAppearance;
import net.artelnatif.nicko.gui.items.main.SettingsSubGUI;
import net.artelnatif.nicko.gui.items.skin.ChangeName;
import net.artelnatif.nicko.gui.items.skin.ChangeNameAndSkin;
import net.artelnatif.nicko.gui.items.skin.ChangeSkin;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class MainGUI {
    private final Player player;
    private final Gui gui;

    public MainGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# # # N B S # # #",
                "E P A # # # # # R"};

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

    public void open() {
        Window.single().setGui(gui).setTitle("Nicko - Home").open(player);
    }
}
