package xyz.ineanto.nicko.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.items.admin.check.PlayerInformationItem;
import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.gui.items.common.ScrollDownItem;
import xyz.ineanto.nicko.gui.items.common.ScrollUpItem;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlayerCheckGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public PlayerCheckGUI(Player player, Collection<? extends Player> players) {
        final I18N i18n = new I18N(player);
        this.title = i18n.translatePrefixless(I18NDict.GUI.Titles.CHECK);

        final List<Item> items = players.stream()
                .map(Entity::getUniqueId)
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(mappedPlayer -> new PlayerInformationItem(i18n, mappedPlayer))
                .collect(Collectors.toList());

        final AdminGUI parent = new AdminGUI(player);
        final GoBackItem backItem = new GoBackItem(player);
        final ScrollUpItem scrollUpItem = new ScrollUpItem(i18n);
        final ScrollDownItem scrollDownItem = new ScrollDownItem(i18n);

        gui = ScrollGui.items(guiItemBuilder -> {
            guiItemBuilder.setStructure(
                    "x x x x x x x x U",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x D",
                    "B % % % % % % % %");
            guiItemBuilder.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
            guiItemBuilder.addIngredient('U', scrollUpItem);
            guiItemBuilder.addIngredient('D', scrollDownItem);
            guiItemBuilder.addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()));
            guiItemBuilder.setContent(items);
        });

        this.player = player;
    }

    public void open() {
        final Window.Builder.Normal.Single window = Window.single().setGui(gui).setTitle(title);
        window.addOpenHandler(() -> PlayerCheckGUIData.VIEWERS.add(player.getUniqueId()));
        window.addCloseHandler(() -> PlayerCheckGUIData.VIEWERS.remove(player.getUniqueId()));
        window.open(player);
    }
}