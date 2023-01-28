package net.artelnatif.nicko.gui.items.main;

import de.studiocode.invui.item.ItemProvider;
import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.impl.BaseItem;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class ResetAppearance extends BaseItem {
    @Override
    public ItemProvider getItemProvider() {
        final ItemBuilder builder = new ItemBuilder(Material.TNT);
        builder.setDisplayName("§fReset");
        builder.addLoreLines("§7Get rid of your disguise.");
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            final AppearanceManager appearanceManager = AppearanceManager.get(player);

            if (!appearanceManager.hasData()) {
                player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.NONE));
                event.getView().close();
                return;
            }

            if (!appearanceManager.reset().isError()) {
                player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.SUCCESS));
            } else {
                player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.FAIL));
            }
        }
    }
}
