package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.GoBack;
import net.artelnatif.nicko.gui.items.skin.ChangeSkin;
import net.artelnatif.nicko.gui.items.skin.ChangeName;
import net.artelnatif.nicko.gui.items.skin.ChangeNameAndSkin;
import org.bukkit.entity.Player;

public class AppearanceManagerGUI {
    private final Player player;
    private final GUI gui;

    public AppearanceManagerGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(
                        "# # # # # # # # #",
                        "# % % % % % % % #",
                        "# % # N A S # % #",
                        "# % % % % % % % #",
                        "B # # # # # # # #"
                )
                .addIngredient('N', new ChangeName())
                .addIngredient('A', new ChangeNameAndSkin())
                .addIngredient('S', new ChangeSkin())
                .addIngredient('B', new GoBack(new MainGUI(player).getGUI()))
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
