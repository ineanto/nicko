package xyz.atnrch.nicko.gui.admin;

import xyz.atnrch.nicko.gui.AdminGUI;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheDetailed;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheInvalidate;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheOverview;
import xyz.atnrch.nicko.gui.items.common.GoBack;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class CacheManagementGUI {
    public static final String TITLE = "Nicko > Admin... > Cache";

    private final Player player;
    private final Gui gui;

    public CacheManagementGUI(Player player) {
        this.gui = Gui.normal()
                .setStructure("B # S A D")
                .addIngredient('B', new GoBack(new AdminGUI(player).getGUI()))
                .addIngredient('S', new CacheOverview())
                .addIngredient('A', new CacheInvalidate())
                .addIngredient('D', new CacheDetailed())
                .build();
        this.player = player;
    }

    public Gui getGUI() {
        return gui;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(TITLE).open(player);
    }
}
