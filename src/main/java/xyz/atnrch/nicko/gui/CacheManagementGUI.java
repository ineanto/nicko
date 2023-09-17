package xyz.atnrch.nicko.gui;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheStatisticsItem;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateCacheItem;
import xyz.atnrch.nicko.gui.items.admin.cache.InvalidateSkinItem;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class CacheManagementGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public CacheManagementGUI(Player player) {
        final I18N i18n = new I18N(player);
        this.title = i18n.translatePrefixless(I18NDict.GUI.Titles.CACHE);

        final AdminGUI parent = new AdminGUI(player);
        final GoBackItem backItem = new GoBackItem(player);

        final CacheStatisticsItem cacheStatisticsItem = new CacheStatisticsItem(player);
        final InvalidateCacheItem invalidateCacheItem = new InvalidateCacheItem(player);
        final InvalidateSkinItem invalidateSkinItem = new InvalidateSkinItem(player);

        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S C E # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
                .addIngredient('S', cacheStatisticsItem.get())
                .addIngredient('C', invalidateCacheItem.get())
                .addIngredient('E', invalidateSkinItem.get())
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
