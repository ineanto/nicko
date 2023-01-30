package net.artelnatif.nicko.bukkit.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CacheOverview extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
        final LoadingCache<String, Optional<MojangSkin>> cache = NickoBukkit.getInstance().getNicko().getMojangAPI().getCache();
        final CacheStats stats = cache.stats();
        builder.setDisplayName("§6Skin cache §foverview:");
        builder.addLoreLines(
                "Request Count: §2" + stats.requestCount(),
                "Skin Cached: §2" + Math.round(cache.size()),
                "§7§oCache is cleared every 24 hours.",
                "§7§o(Click to refresh)");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            notifyWindows();
        }
    }
}