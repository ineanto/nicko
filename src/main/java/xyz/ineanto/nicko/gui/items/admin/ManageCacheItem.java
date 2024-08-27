package xyz.ineanto.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.gui.CacheManagementGUI;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class ManageCacheItem extends AsyncItem {
    public ManageCacheItem(PlayerLanguage playerLanguage) {
        super(new SuppliedItem(() -> {
                    final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
                    return playerLanguage.translateItem(builder, LanguageKey.GUI.LOADING);
                }, (click -> true)).getItemProvider(),
                () -> {
                    try {
                        final SkullBuilder builder = new SkullBuilder("Notch");
                        return playerLanguage.translateItem(builder, LanguageKey.GUI.Admin.MANAGE_CACHE);
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        Nicko.getInstance().getLogger().warning("Unable to get Head texture for Notch! (GUI/ManageCache)");
                        return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Admin.MANAGE_CACHE);
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
