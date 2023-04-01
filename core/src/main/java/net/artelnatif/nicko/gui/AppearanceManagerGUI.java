package net.artelnatif.nicko.gui;

import net.artelnatif.nicko.gui.items.common.GoBack;
import net.artelnatif.nicko.gui.items.skin.ChangeName;
import net.artelnatif.nicko.gui.items.skin.ChangeNameAndSkin;
import net.artelnatif.nicko.gui.items.skin.ChangeSkin;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class AppearanceManagerGUI {
    private final Player player;
    private final Gui gui;

    public AppearanceManagerGUI(Player player) {
        this.gui = Gui.normal()
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
        Window.single().setGui(gui).setTitle("Nicko").open(player);
    }
}
