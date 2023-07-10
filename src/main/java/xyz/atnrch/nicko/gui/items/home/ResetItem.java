package xyz.atnrch.nicko.gui.items.home;

import xyz.atnrch.nicko.appearance.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ResetItem extends SuppliedItem {
    public ResetItem() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            builder.setDisplayName("Reset appearance");
            builder.addLoreLines("§7Completely remove your disguise.");
            return builder;
        }, (event) -> {
            final Player player = event.getPlayer();
            final I18N i18n = new I18N(player);

            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final AppearanceManager appearanceManager = AppearanceManager.get(player);

                if (!appearanceManager.hasData()) {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.MISSING));
                    event.getEvent().getView().close();
                    return true;
                }

                if (!appearanceManager.reset().isError()) {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.OK));
                    return true;
                } else {
                    player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.ERROR));
                    return false;
                }
            }
            return false;
        });
    }
}
