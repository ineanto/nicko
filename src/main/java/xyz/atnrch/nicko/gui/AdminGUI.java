package xyz.atnrch.nicko.gui;

import xyz.atnrch.nicko.gui.items.admin.ManageCacheItem;
import xyz.atnrch.nicko.gui.items.admin.ManagePlayerItem;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.common.UnavailableItem;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

public class AdminGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public AdminGUI(Player player) {
        final I18N i18n = new I18N(player);
        this.title = i18n.translatePrefixless(I18NDict.GUI.Admin.TITLE);

        final HomeGUI parent = new HomeGUI(player);
        final GoBackItem backItem = new GoBackItem(player);
        final UnavailableItem unavailableItem = new UnavailableItem(player);

        this.gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # S C U # # #",
                        "B # # # # # # # #"
                )
                .addIngredient('S', new ManageCacheItem())
                .addIngredient('U', unavailableItem.get())
                .addIngredient('C', new ManagePlayerItem())
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
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
