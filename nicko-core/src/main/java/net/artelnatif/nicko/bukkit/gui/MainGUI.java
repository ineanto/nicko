package net.artelnatif.nicko.bukkit.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.bukkit.gui.items.main.AdminSubGUI;
import net.artelnatif.nicko.bukkit.gui.items.main.AppearanceManagerSubGUI;
import net.artelnatif.nicko.bukkit.gui.items.main.ResetAppearance;
import net.artelnatif.nicko.bukkit.gui.items.main.SettingsSubGUI;
import org.bukkit.entity.Player;

public class MainGUI {
    private final Player player;
    private final GUI gui;

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

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(dynamicStructure)
                .addIngredient('R', new ResetAppearance())
                .addIngredient('S', new AppearanceManagerSubGUI())
                .addIngredient('P', new SettingsSubGUI())
                .addIngredient('A', new AdminSubGUI())
                .build();
        this.player = player;
    }

    public GUI getGUI() {
        return gui;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
