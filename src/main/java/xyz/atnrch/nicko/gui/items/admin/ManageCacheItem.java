package xyz.atnrch.nicko.gui.items.admin;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.gui.CacheManagementGUI;
import xyz.atnrch.nicko.gui.items.common.LoadingItem;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

public class ManageCacheItem extends AsyncItem {
    public ManageCacheItem(I18N i18n) {
        super(new LoadingItem(i18n).get(),
                () -> {
                    final SkullBuilder builder = new SkullBuilder("Notch");
                    final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.MANAGE_CACHE);
                    builder.setDisplayName(translation.getName());
                    translation.getLore().forEach(builder::addLoreLines);
                    return builder;
                });
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            new CacheManagementGUI(player).open();
        }
    }
}
