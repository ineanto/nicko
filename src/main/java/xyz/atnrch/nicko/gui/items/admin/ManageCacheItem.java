package xyz.atnrch.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.CacheManagementGUI;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class ManageCacheItem extends AsyncItem {
    public ManageCacheItem(I18N i18n) {
        super(new SuppliedItem(() -> {
                    final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
                    final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.LOADING);
                    builder.setDisplayName(translation.getName());
                    translation.getLore().forEach(builder::addLoreLines);
                    return builder;
                }, (click -> true)).getItemProvider(),
                () -> {
                    try {
                        final SkullBuilder builder = new SkullBuilder("Notch");
                        final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.MANAGE_CACHE);
                        builder.setDisplayName(translation.getName());
                        translation.getLore().forEach(builder::addLoreLines);
                        return builder;
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        final ItemBuilder builder = new ItemBuilder(Material.TNT);
                        final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.ERROR);
                        builder.setDisplayName(translation.getName());
                        translation.getLore().forEach(builder::addLoreLines);
                        NickoBukkit.getInstance().getLogger().warning("Unable to get Head texture for Notch! (GUI/ManageCache)");
                        return builder;
                    }
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
