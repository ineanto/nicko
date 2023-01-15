package net.artelnatif.nicko.gui.sub;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.MainGUI;
import net.artelnatif.nicko.gui.items.common.BackItem;
import net.artelnatif.nicko.gui.items.skin.ChangeNameAndSkinItem;
import net.artelnatif.nicko.gui.items.skin.ChangeNameItem;
import net.artelnatif.nicko.gui.items.skin.ChangeSkinItem;
import org.bukkit.entity.Player;

public class AppearanceGUI {
    private final Player player;
    private final GUI gui;
    private final String[] structure = new String[]{
            "# # # # # # # # #",
            "# % % % % % % % #",
            "# % # N A S # % #",
            "# % % % % % % % #",
            "B # # # # # # # #"};

    public AppearanceGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(new Structure(structure))
                .addIngredient('N', new ChangeNameItem())
                .addIngredient('A', new ChangeNameAndSkinItem())
                .addIngredient('S', new ChangeSkinItem())
                .addIngredient('B', new BackItem(new MainGUI(player).getGUI()))
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
