package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.main.*;
import org.bukkit.entity.Player;

public class MainGUI {
    private final Player player;
    private final GUI gui;
    private final String[] structureIngredients = new String[]
            {"# # # # # # # # #",
                    "# % % % % % % % #",
                    "# % # R B # # % #",
                    "# % # N A S # % #",
                    "# % % % % % % % #",
                    "E # # # # # # # #"};

    public MainGUI(Player player) {
        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            structureIngredients[3] = structureIngredients[3].replace("A", "#");
        }

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(new Structure(structureIngredients))
                .addIngredient('E', new ExitDoorItem())
                .addIngredient('N', new ChangeNameItem())
                .addIngredient('S', new ChangeSkinItem())
                .addIngredient('A', new AdminPanelAccessItem())
                .addIngredient('B', new ChangeNameAndSkinItem())
                .addIngredient('R', new ResetItem())
                // TODO: 11/3/22 Add possibility to reset either skin or name or both
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
