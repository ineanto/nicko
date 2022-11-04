package net.artelnatif.nicko.gui.items.common;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class BackItem extends BaseItem {
    private final GUI gui;

    public BackItem(GUI gui) {
        this.gui = gui;
    }

    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.ARROW);
        builder.setDisplayName("Go back");
        builder.addLoreLines("§7Return to the previous window.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        event.getView().close();
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
