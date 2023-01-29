package net.artelnatif.nicko.bukkit.gui.items.admin;

import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.AsyncItem;
import net.artelnatif.nicko.bukkit.gui.admin.CacheManagementGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ManageCache extends AsyncItem {
    public ManageCache() {
        super(new ItemBuilder(Material.PAINTING)
                        .setDisplayName("§fManage §6skin §fcache...")
                        .addLoreLines("§7Access the skin cache management panel."),
                () -> {
                    final SkullBuilder builder = new SkullBuilder("Notch");
                    builder.setDisplayName("§fManage §6skin §fcache...");
                    builder.addLoreLines("§7Access the skin cache management panel.");
                    return builder;
                });
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new CacheManagementGUI(player).open();
        }
    }
}
