package net.artelnatif.nicko.gui.items.invalidator;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InvalidateAllItem extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.BARRIER);
        builder.setDisplayName("§fInvalidate §6all §fthe skin cache");
        builder.addLoreLines(
                "§c§oNOT RECOMMENDED",
                "§7Invalidate every skin entry present in the cache,",
                "§7without removing active player disguises.",
                "§7Skins will have to be fetched again if asked by a disguise.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            player.sendMessage(I18N.translate(player, I18NDict.Event.Admin.CACHE_CLEAN));
            NickoBukkit.getInstance().getMojangAPI().getCache().invalidateAll();
        }
    }
}
