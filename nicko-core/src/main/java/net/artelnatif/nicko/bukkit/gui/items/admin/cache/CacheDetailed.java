package net.artelnatif.nicko.bukkit.gui.items.admin.cache;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.bukkit.gui.admin.cache.CacheDetailledGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class CacheDetailed extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.PAPER);
        builder.setDisplayName("§6Invalidate specific skin...");
        builder.addLoreLines("§7Select a specific skin to invalidate.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new CacheDetailledGUI(player).open();
        }
    }
}
