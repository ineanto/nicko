package xyz.atnrch.nicko.gui;

import xyz.atnrch.nicko.gui.items.admin.ManageCache;
import xyz.atnrch.nicko.gui.items.common.GoBack;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class AdminGUI {
    private final String title = "Nicko > Administration";
    private final Player player;
    private final Gui gui;

    public AdminGUI(Player player) {
        final MainGUI parent = new MainGUI(player);
        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S U U # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('S', new ManageCache())
                .addIngredient('B', new GoBack(parent.getGUI(), title))
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
