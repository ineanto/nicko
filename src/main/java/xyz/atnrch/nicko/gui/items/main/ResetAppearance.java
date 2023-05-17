package xyz.atnrch.nicko.gui.items.main;

import xyz.atnrch.nicko.disguise.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ResetAppearance extends SuppliedItem {
    public ResetAppearance() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            builder.setDisplayName("§fReset");
            builder.addLoreLines("§7Get rid of your disguise.");
            return builder;
        }, (event) -> {
            final Player player = event.getPlayer();
            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final AppearanceManager appearanceManager = AppearanceManager.get(player);

                if (!appearanceManager.hasData()) {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.NONE));
                    event.getEvent().getView().close();
                    return true;
                }

                if (!appearanceManager.reset().isError()) {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.SUCCESS));
                    return true;
                } else {
                    player.sendMessage(I18N.translate(player, I18NDict.Event.Undisguise.FAIL));
                    return false;
                }
            }
            return false;
        });
    }
}
