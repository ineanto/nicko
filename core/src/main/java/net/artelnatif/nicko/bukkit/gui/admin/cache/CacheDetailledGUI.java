package net.artelnatif.nicko.bukkit.gui.admin.cache;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.gui.structure.Markers;
import de.studiocode.invui.item.Item;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.bukkit.gui.items.admin.cache.SkinPlaceholder;
import net.artelnatif.nicko.bukkit.gui.admin.CacheManagementGUI;
import net.artelnatif.nicko.bukkit.gui.items.common.GoBack;
import net.artelnatif.nicko.bukkit.gui.items.common.ScrollDown;
import net.artelnatif.nicko.bukkit.gui.items.common.ScrollUp;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class CacheDetailledGUI {
    private final Player player;
    private final GUI gui;

    public CacheDetailledGUI(Player player) {
        final ConcurrentMap<String, Optional<MojangSkin>> skins = NickoBukkit.getInstance().getNicko().getMojangAPI().getCache().asMap();
        final List<String> loadedSkins = skins.entrySet().stream()
                .filter(entry -> entry.getValue().isPresent())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        final List<Item> items = loadedSkins.stream()
                .map(SkinPlaceholder::new)
                .collect(Collectors.toList());

        this.gui = new GUIBuilder<>(GUIType.SCROLL_ITEMS)
                .setStructure(
                        "% # # # # # # # %",
                        "# x x x x x x U #",
                        "# x x x x x x # #",
                        "# x x x x x x # #",
                        "# x x x x x x D #",
                        "B # # # # # # # %"
                )
                .addIngredient('x', Markers.ITEM_LIST_SLOT_HORIZONTAL)
                .addIngredient('U', new ScrollUp())
                .addIngredient('D', new ScrollDown())
                .addIngredient('B', new GoBack(new CacheManagementGUI(player).getGUI()))
                .setItems(items)
                .build();
        this.player = player;
    }

    public GUI getGUI() {
        return gui;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
