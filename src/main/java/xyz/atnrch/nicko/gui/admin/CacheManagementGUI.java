package xyz.atnrch.nicko.gui.admin;

import xyz.atnrch.nicko.gui.AdminGUI;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateSpecificEntry;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateCompleteCache;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheOverview;
import xyz.atnrch.nicko.gui.items.common.GoBack;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class CacheManagementGUI {
    private final String title = "Nicko > Admin... > Cache";
    private final Player player;
    private final Gui gui;

    public CacheManagementGUI(Player player) {
        final AdminGUI parent = new AdminGUI(player);
        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S A D # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('B', new GoBack(parent.getGUI(), parent.getTitle()))
                .addIngredient('S', new CacheOverview())
                .addIngredient('A', new InvalidateCompleteCache())
                .addIngredient('D', new InvalidateSpecificEntry())
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
