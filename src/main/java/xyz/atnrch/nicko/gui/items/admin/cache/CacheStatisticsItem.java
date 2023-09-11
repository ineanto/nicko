package xyz.atnrch.nicko.gui.items.admin.cache;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.atnrch.nicko.mojang.MojangSkin;
import org.bukkit.Material;
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

            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.Cache.STATISTICS,
                    stats.requestCount(),
                    Math.round(cache.size())
            );
            translation.getLore().forEach(System.out::println);
            // TODO (Ineanto, 9/11/23): This doesn't work.
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, (event) -> true);
    }
}