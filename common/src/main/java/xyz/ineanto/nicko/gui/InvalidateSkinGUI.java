package xyz.ineanto.nicko.gui;

import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.gui.items.common.ScrollUpItem;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.ineanto.nicko.Nicko;
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
        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.INVALIDATE_SKIN, false);

        final ConcurrentMap<String, Optional<MojangSkin>> skins = Nicko.getInstance().getMojangAPI().getSkinCache().asMap();
        final List<String> loadedSkins = skins.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(Map.Entry::getKey)
                .toList();

        final List<Item> items = loadedSkins.stream()
                .map(uuid -> new CacheEntryItem(playerLanguage, uuid))
                .collect(Collectors.toList());

        final CacheManagementGUI parent = new CacheManagementGUI(player);
        final ScrollUpItem scrollUpItem = new ScrollUpItem(playerLanguage);
        final ScrollDownItem scrollDownItem = new ScrollDownItem(playerLanguage);
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
