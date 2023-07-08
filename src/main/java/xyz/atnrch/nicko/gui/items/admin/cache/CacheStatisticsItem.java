package xyz.atnrch.nicko.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.mojang.MojangSkin;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class CacheStatisticsItem extends SuppliedItem {
    public CacheStatisticsItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.BOOK);
            final LoadingCache<String, Optional<MojangSkin>> cache = NickoBukkit.getInstance().getMojangAPI().getSkinCache();
            final CacheStats stats = cache.stats();
            builder.setDisplayName("§fStatistics");
            builder.addLoreLines(
                    "Request Count: §b" + stats.requestCount(),
                    "Skin Cached: §b" + Math.round(cache.size()),
                    "§8§oCache is cleared every 24 hours.");
            return builder;
        }, (event) -> true);
    }
}