package xyz.ineanto.nicko.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class CacheStatisticsItem {
    private final I18N i18n;

    public CacheStatisticsItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.BOOK);
            final LoadingCache<String, Optional<MojangSkin>> cache = NickoBukkit.getInstance().getMojangAPI().getSkinCache();
            final CacheStats stats = cache.stats();

            return i18n.translateItem(builder, I18NDict.GUI.Admin.Cache.STATISTICS,
                    stats.requestCount(),
                    Math.round(cache.size())
            );
        }, (event) -> true);
    }
}