package net.artelnatif.nicko.gui;

import net.artelnatif.nicko.gui.items.admin.ManageCache;
import net.artelnatif.nicko.gui.items.common.GoBack;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class AdminGUI {
    private final Player player;
    private final Gui gui;

    public AdminGUI(Player player) {
        this.gui = Gui.normal()
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

    public Gui getGUI() {
        return gui;
    }

    public void open() {
        Window.single().setGui(gui).setTitle("Nicko").open(player);
    }
}
