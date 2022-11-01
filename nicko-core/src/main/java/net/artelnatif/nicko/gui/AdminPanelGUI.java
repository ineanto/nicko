package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.entity.Player;

public class AdminPanelGUI {
    private final Player player;
    private final GUI gui;
    private String[] structureIngredients = new String[]{"# # # # # # # # #",
            "# % % % % % % % #",
            "# % # # B # # % #",
            "# % # N A S # % #",
            "# % % % % % % % #",
            "E # # # # # # # #"};

    public AdminPanelGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(new Structure(structureIngredients))
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
