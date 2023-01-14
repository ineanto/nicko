package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Structure;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.main.AdminItem;
import net.artelnatif.nicko.gui.items.main.ResetItem;
import net.artelnatif.nicko.gui.items.main.SettingsItem;
import net.artelnatif.nicko.gui.items.main.SkinItem;
import org.bukkit.entity.Player;

public class MainGUI {
    private final Player player;
    private final GUI gui;
    private final String structure = """
            # # # # # # # # #
            # % % % % % % % #
            # % # R S P # % #
            # % % % A % % % #
            E # # # # # # # #
            """;

    public MainGUI(Player player) {
        if (!player.hasPermission("nicko.admin") || !player.isOp()) {
            final String[] rows = structure.split("\n");
            rows[3] = rows[3].replace("A", "#");
        }

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(new Structure(structure))
                .addIngredient('R', new ResetItem())
                .addIngredient('S', new SkinItem())
                .addIngredient('P', new SettingsItem())
                .addIngredient('A', new AdminItem())
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
