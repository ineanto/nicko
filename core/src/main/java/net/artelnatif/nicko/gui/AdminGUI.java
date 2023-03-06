package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.GoBack;
import net.artelnatif.nicko.gui.items.admin.ManageCache;
import org.bukkit.entity.Player;

public class AdminGUI {
    private final Player player;
    private final GUI gui;

    public AdminGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# % % X X S % % #",
                        "B # # # # # # # #"
                )
                .addIngredient('S', new ManageCache())
                .addIngredient('B', new GoBack(new MainGUI(player).getGUI()))
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
