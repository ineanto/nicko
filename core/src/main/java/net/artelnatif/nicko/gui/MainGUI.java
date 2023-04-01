package net.artelnatif.nicko.gui;

import net.artelnatif.nicko.gui.items.main.AdminSubGUI;
import net.artelnatif.nicko.gui.items.main.AppearanceManagerSubGUI;
import net.artelnatif.nicko.gui.items.main.ResetAppearance;
import net.artelnatif.nicko.gui.items.main.SettingsSubGUI;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class MainGUI {
    private final Player player;
    private final Gui gui;

    public MainGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# % % % A % % % #",
                "# % # R S P # % #",
                "# % % % % % % % #",
                "E # # # # # # # #"};

        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            dynamicStructure[3] = dynamicStructure[3].replace("A", "#");
        }

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('R', new ResetAppearance())
                .addIngredient('S', new AppearanceManagerSubGUI())
                .addIngredient('P', new SettingsSubGUI())
                .addIngredient('A', new AdminSubGUI())
                .build();
        this.player = player;
    }

    public Gui getGUI() {
        return gui;
    }

    public void open() {
        Window.single().setGui(gui).setTitle("Nicko").open(player);
    }
}
