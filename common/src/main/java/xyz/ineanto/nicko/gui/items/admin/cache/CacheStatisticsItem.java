package xyz.ineanto.nicko.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangSkin;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class CacheStatisticsItem {
    private final PlayerLanguage playerLanguage;

    public CacheStatisticsItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.BOOK);
            final LoadingCache<String, Optional<MojangSkin>> cache = Nicko.getInstance().getMojangAPI().getSkinCache();
            final CacheStats stats = cache.stats();

            return playerLanguage.translateItem(builder, LanguageKey.GUI.Admin.Cache.STATISTICS,
                    stats.requestCount(),
                    Math.round(cache.size())
            );
        }, (event) -> true);
    }
}