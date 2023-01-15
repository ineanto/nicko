package net.artelnatif.nicko.gui.sub;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.MainGUI;
import net.artelnatif.nicko.gui.items.common.BackItem;
import org.bukkit.entity.Player;

public class AdminPanelGUI {
    private final Player player;
    private final GUI gui;
    private final String[] structure = new String[]{
            "# # # # # # # # #",
            "# % % X X X % % #",
            "B # # # # # # # #"
    };

    public AdminPanelGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structure)
                .addIngredient('B', new BackItem(new MainGUI(player).getGUI()))
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
