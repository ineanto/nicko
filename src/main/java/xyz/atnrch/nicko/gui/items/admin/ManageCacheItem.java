package xyz.atnrch.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.CacheManagementGUI;
import xyz.atnrch.nicko.gui.items.ItemDefaults;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
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
                    return i18n.translateItem(builder, I18NDict.GUI.LOADING);
                }, (click -> true)).getItemProvider(),
                () -> {
                    try {
                        final SkullBuilder builder = new SkullBuilder("Notch");
                        return i18n.translateItem(builder, I18NDict.GUI.Admin.MANAGE_CACHE);
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        NickoBukkit.getInstance().getLogger().warning("Unable to get Head texture for Notch! (GUI/ManageCache)");
                        return ItemDefaults.getErrorSkullItem(i18n, I18NDict.GUI.Admin.MANAGE_CACHE);
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
