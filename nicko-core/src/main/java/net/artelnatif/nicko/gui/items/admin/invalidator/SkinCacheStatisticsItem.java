package net.artelnatif.nicko.gui.items.admin.invalidator;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SkinCacheStatisticsItem extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.OAK_SIGN);
        final LoadingCache<String, Optional<MojangSkin>> cache = NickoBukkit.getInstance().getMojangAPI().getCache();
        final CacheStats stats = cache.stats();
        builder.addEnchantment(Enchantment.DAMAGE_ALL, 1, false);
        builder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        builder.setDisplayName("§6Skin Cache overview:");
        builder.addLoreLines(
                "§d§oCache is cleared every 24 hours.",
                "§6Average Load Penalty: §d" + Math.round(stats.averageLoadPenalty()),
                "§6Request Count: §d" + stats.requestCount(),
                "§6Skin Cached: §d" + Math.round(cache.size()),
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