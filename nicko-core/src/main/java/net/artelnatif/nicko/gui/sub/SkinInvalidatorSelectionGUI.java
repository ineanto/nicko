package net.artelnatif.nicko.gui.sub;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.BackItem;
import net.artelnatif.nicko.gui.items.invalidator.InvalidateAllItem;
import org.bukkit.entity.Player;

public class SkinInvalidatorSelectionGUI {
    private final Player player;
    private final GUI gui;
    private final String[] structure = new String[]{
            "B # # A S",
    };

    public SkinInvalidatorSelectionGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structure)
                .addIngredient('B', new BackItem(new AdminPanelGUI(player).getGUI()))
                .addIngredient('A', new InvalidateAllItem())
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
