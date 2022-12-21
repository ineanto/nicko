package net.artelnatif.nicko.gui.items.admin;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class ReloadLanguageFileItem extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.BOOK)
                .addEnchantment(Enchantment.DAMAGE_ALL, 1, false)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .setDisplayName("§fReload Language File");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            final Properties properties = NickoBukkit.getInstance().getLocaleManager().reloadCustomLanguageFile();
            if (properties != null) {
                player.sendMessage(I18N.translate(player, I18NDict.Plugin.CUSTOM_LANGUAGE_RELOAD_SUCCESS));
            } else {
                player.sendMessage(I18N.translate(player, I18NDict.Plugin.CUSTOM_LANGUAGE_RELOAD_FAIL));
            }
        }
    }
}