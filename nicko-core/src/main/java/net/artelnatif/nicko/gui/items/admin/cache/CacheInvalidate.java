package net.artelnatif.nicko.gui.items.admin.cache;

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

public class CacheInvalidate extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.TNT);
        builder.setDisplayName("§6Invalidate §fskin cache");
        builder.addLoreLines(
                "§c§oNOT RECOMMENDED",
                "§7Invalidates every skin entry present in the cache.",
                "§7Does not reset player disguises.",
                "§7Could be useful if a skin has been updated",
                "§7recently and the cache is now outdated.");
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
