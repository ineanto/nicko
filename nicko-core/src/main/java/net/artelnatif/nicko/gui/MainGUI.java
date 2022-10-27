package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.SimpleItem;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MainGUI {
    private final Player player;
    private final GUI gui;
    private String[] structureIngredients = new String[]{"# # # # # # # # #",
            "# % % % % % % % #",
            "# % # # B # # % #",
            "# % # N A S # % #",
            "# % % % % % % % #",
            "E # # # # # # # #"};

    public MainGUI(Player player) {
        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            structureIngredients[3] = structureIngredients[3].replace("A", "#");
        }

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(new Structure(structureIngredients))
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE)))
                .addIngredient('%', new SimpleItem(new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE)))
                .addIngredient('E', new ExitDoorItem())
                .addIngredient('N', new ChangeNameItem())
                .addIngredient('S', new ChangeSkinItem())
                .addIngredient('A', new AdminPanelAccessItem())
                .addIngredient('B', new ChangeNameAndSkinItem())
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
