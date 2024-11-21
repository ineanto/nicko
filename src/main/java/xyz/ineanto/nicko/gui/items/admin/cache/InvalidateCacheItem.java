package xyz.ineanto.nicko.gui.items.admin.cache;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateCacheItem {
    private final PlayerLanguage playerLanguage;

    public InvalidateCacheItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Admin.Cache.INVALIDATE_CACHE);
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();

                final Player player = click.getPlayer();
                final PlayerLanguage playerLanguage = new PlayerLanguage(player);
                player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Admin.Cache.INVALIDATE_CACHE));
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1f);
                Nicko.getInstance().getMojangAPI().getSkinCache().invalidateAll();
                return true;
            }
            return false;
        });
    }
}
