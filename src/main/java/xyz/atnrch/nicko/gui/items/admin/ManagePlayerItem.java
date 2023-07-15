package xyz.atnrch.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.gui.PlayerCheckGUI;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ManagePlayerItem extends SuppliedItem {
    public ManagePlayerItem() {
        super(() -> {
                    final ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
                    builder.setDisplayName("Check a player...");
                    builder.addLoreLines("§7See players' disguise information.");
                    return builder;
                },
                click -> true);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent
            event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new PlayerCheckGUI(player).open();
        }
    }
}
