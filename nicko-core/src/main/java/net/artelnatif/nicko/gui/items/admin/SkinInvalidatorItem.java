package net.artelnatif.nicko.gui.items.admin;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.gui.sub.SkinInvalidatorSelectionGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class SkinInvalidatorItem extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final SkullBuilder builder = new SkullBuilder("Notch");
        builder.setDisplayName("§fManage §6skin §fcache...");
        builder.addLoreLines("§7Access the skin cache management panel.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new SkinInvalidatorSelectionGUI(player).open();
        }
    }
}
