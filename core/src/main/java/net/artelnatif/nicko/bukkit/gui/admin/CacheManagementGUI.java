package net.artelnatif.nicko.bukkit.gui.admin;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.bukkit.gui.AdminGUI;
import net.artelnatif.nicko.bukkit.gui.items.admin.cache.CacheDetailed;
import net.artelnatif.nicko.bukkit.gui.items.admin.cache.CacheInvalidate;
import net.artelnatif.nicko.bukkit.gui.items.admin.cache.CacheOverview;
import net.artelnatif.nicko.bukkit.gui.items.common.GoBack;
import org.bukkit.entity.Player;

public class CacheManagementGUI {
    private final Player player;
    private final GUI gui;

    public CacheManagementGUI(Player player) {
        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure("B # S A D")
                .addIngredient('B', new GoBack(new AdminGUI(player).getGUI()))
                .addIngredient('S', new CacheOverview())
                .addIngredient('A', new CacheInvalidate())
                .addIngredient('D', new CacheDetailed())
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
