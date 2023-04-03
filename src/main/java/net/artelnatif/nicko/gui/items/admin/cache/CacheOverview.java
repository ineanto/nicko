package net.artelnatif.nicko.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class CacheOverview extends SuppliedItem {
    public CacheOverview() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
            final LoadingCache<String, Optional<MojangSkin>> cache = NickoBukkit.getInstance().getMojangAPI().getCache();
            final CacheStats stats = cache.stats();
            builder.setDisplayName("§6Skin cache §foverview:");
            builder.addLoreLines(
                    "Request Count: §2" + stats.requestCount(),
                    "Skin Cached: §2" + Math.round(cache.size()),
                    "§7§oCache is cleared every 24 hours.",
                    "§7§o(Click to refresh)");
            return builder;
        }, (event) -> true);
    }
}