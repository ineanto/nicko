package xyz.atnrch.nicko.gui.admin.cache;

import xyz.atnrch.nicko.gui.items.common.GoBack;
import xyz.atnrch.nicko.gui.items.common.ScrollUp;
import xyz.atnrch.nicko.mojang.MojangSkin;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.items.admin.cache.CacheEntry;
import xyz.atnrch.nicko.gui.admin.CacheManagementGUI;
import xyz.atnrch.nicko.gui.items.common.ScrollDown;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class CacheDetailedGUI {
    public static final String TITLE = "... > Cache > Invalidate";

    private final Player player;
    private final Gui gui;

    public CacheDetailedGUI(Player player) {
        final ConcurrentMap<String, Optional<MojangSkin>> skins = NickoBukkit.getInstance().getMojangAPI().getCache().asMap();
        final List<String> loadedSkins = skins.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        final List<Item> items = loadedSkins.stream()
                .map(CacheEntry::new)
                .collect(Collectors.toList());

        final CacheManagementGUI parent = new CacheManagementGUI(player);
        gui = ScrollGui.items(guiItemBuilder -> {
            guiItemBuilder.setStructure(
                    "x x x x x x x x U",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x D",
                    "B % % % % % % % %");
            guiItemBuilder.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
            guiItemBuilder.addIngredient('U', new ScrollUp());
            guiItemBuilder.addIngredient('D', new ScrollDown());
            guiItemBuilder.addIngredient('B', new GoBack(parent.getGUI(), parent.getTitle()));
            guiItemBuilder.setContent(items);
        });

        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(TITLE).open(player);
    }
}
