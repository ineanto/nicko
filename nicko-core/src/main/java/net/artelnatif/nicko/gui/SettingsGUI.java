package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.BackItem;
import org.bukkit.entity.Player;

public class SettingsGUI {
    private final Player player;
    private final GUI gui;

    public SettingsGUI(Player player) {
        final Structure structure = new Structure("# # # # # # # # #",
                "# % % M C R % % #",
                "B # # # # # # # #");
        structure.addIngredient('B', new BackItem(new MainGUI(player).getGUI()));
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structure)
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}