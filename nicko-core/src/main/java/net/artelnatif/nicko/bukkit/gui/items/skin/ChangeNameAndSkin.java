package net.artelnatif.nicko.bukkit.gui.items.skin;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.bukkit.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ChangeNameAndSkin extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.ENDER_PEARL);
        builder.setDisplayName("§6Skin §fand §6name §fchange");
        builder.addLoreLines("§7Change both your skin and name.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            final AnvilManager manager = new AnvilManager(player);
            manager.openNameThenSkinAnvil();
        }
    }
}
