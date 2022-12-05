package net.artelnatif.nicko.gui.items.main;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.gui.AdminPanelGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class AdminPanelAccessItem extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.PISTON);
        builder.setDisplayName("§cAdministration panel...");
        builder.addLoreLines("§7Access the administration panel.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new AdminPanelGUI(player).open();
        }
    }
}
