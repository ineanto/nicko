package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.BackItem;
import org.bukkit.entity.Player;

public class AdminPanelGUI {
    private final Player player;
    private final GUI gui;

    public AdminPanelGUI(Player player) {
        final Structure structure = new Structure("# # # # # # # # #",
                "# % % P U # % % #",
                "B # # # # # # # #");
        structure.addIngredient('B', new BackItem(new MainGUI(player).getGUI()));
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structure)
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
