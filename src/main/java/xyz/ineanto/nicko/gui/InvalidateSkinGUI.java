package xyz.ineanto.nicko.gui;

import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.gui.items.common.ScrollUpItem;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.gui.items.admin.cache.CacheEntryItem;
import xyz.ineanto.nicko.gui.items.common.ScrollDownItem;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class InvalidateSkinGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public InvalidateSkinGUI(Player player) {
        final I18N i18n = new I18N(player);
        this.title = i18n.translateStringWithoutPrefix(I18NDict.GUI.Titles.INVALIDATE_SKIN);

        final ConcurrentMap<String, Optional<MojangSkin>> skins = NickoBukkit.getInstance().getMojangAPI().getSkinCache().asMap();
        final List<String> loadedSkins = skins.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        final List<Item> items = loadedSkins.stream()
                .map(uuid -> new CacheEntryItem(i18n, uuid))
                .collect(Collectors.toList());

        final CacheManagementGUI parent = new CacheManagementGUI(player);
        final ScrollUpItem scrollUpItem = new ScrollUpItem(i18n);
        final ScrollDownItem scrollDownItem = new ScrollDownItem(i18n);
        final GoBackItem backItem = new GoBackItem(player);

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
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
