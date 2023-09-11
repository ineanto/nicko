package xyz.atnrch.nicko.gui.items.admin.cache;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateCacheItem {
    private final I18N i18n;

    public InvalidateCacheItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.Cache.INVALIDATE_CACHE);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();

                final Player player = click.getPlayer();
                final I18N i18n = new I18N(player);
                player.sendMessage(i18n.translate(I18NDict.Event.Admin.Cache.INVALIDATE_CACHE));
                NickoBukkit.getInstance().getMojangAPI().getSkinCache().invalidateAll();
                return true;
            }
            return false;
        });
    }
}
