package xyz.atnrch.nicko.gui;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheStatisticsItem;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateCacheItem;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateEntryItem;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
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
                .addIngredient('B', new GoBackItem(parent.getGUI(), parent.getTitle()))
                .addIngredient('S', new CacheStatisticsItem())
                .addIngredient('A', new InvalidateCacheItem())
                .addIngredient('D', new InvalidateEntryItem())
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
