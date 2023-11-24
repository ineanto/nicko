package xyz.atnrch.nicko.gui;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.ItemDefaults;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import xyz.atnrch.nicko.gui.items.settings.BungeeCordCyclingItem;
import xyz.atnrch.nicko.gui.items.settings.LanguageCyclingItem;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class SettingsGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public SettingsGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# # # L T U # # #",
                "B # # # # # # # #"
        };

        // TODO: 3/6/23 Replace when Redis is not enabled
        dynamicStructure[1] = dynamicStructure[1].replace("T", "U");

        final I18N i18n = new I18N(player);
        this.title = i18n.translatePrefixless(I18NDict.GUI.Titles.SETTINGS);

        final HomeGUI parent = new HomeGUI(player);
        final LanguageCyclingItem languageItem = new LanguageCyclingItem(player);
        final BungeeCordCyclingItem bungeeCordItem = new BungeeCordCyclingItem(player);
        final GoBackItem backItem = new GoBackItem(player);

        this.gui = Gui.normal()
                .setStructure(dynamicStructure)
                .addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()))
                .addIngredient('L', languageItem.get())
                .addIngredient('U', new SimpleItem(ItemDefaults.getUnavailableItem(i18n)))
                .addIngredient('T', bungeeCordItem.get())
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
