package xyz.atnrch.nicko.gui;

import xyz.atnrch.nicko.gui.items.admin.ManageCacheItem;
import xyz.atnrch.nicko.gui.items.admin.ManagePlayerItem;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class AdminGUI {
    private final String title = "Nicko > Administration";
    private final Player player;
    private final Gui gui;

    public AdminGUI(Player player) {
        final HomeGUI parent = new HomeGUI(player);
        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S C U # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('S', new ManageCacheItem())
                .addIngredient('C', new ManagePlayerItem())
                .addIngredient('B', new GoBackItem(parent.getGUI(), parent.getTitle()))
                .build();
        this.player = player;
    }

    public Gui getGUI() {
        return gui;
    }

    public String getTitle() {
        return title;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
