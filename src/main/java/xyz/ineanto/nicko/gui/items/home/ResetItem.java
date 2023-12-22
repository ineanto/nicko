package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class ResetItem {
    private final I18N i18n;

    public ResetItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            return i18n.translateItem(builder, I18NDict.GUI.Home.RESET);
        }, (event) -> {
            final Player player = event.getPlayer();
            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
                optionalProfile.ifPresent(profile -> {
                    if (!profile.hasData()) {
                        player.sendMessage(i18n.translateString(I18NDict.Event.Appearance.Remove.MISSING));
                        event.getEvent().getView().close();
                        return;
                    }

                    final AppearanceManager appearanceManager = new AppearanceManager(player);
                    if (!appearanceManager.reset().isError()) {
                        player.sendMessage(i18n.translateString(I18NDict.Event.Appearance.Remove.OK));
                    } else {
                        player.sendMessage(i18n.translateString(I18NDict.Event.Appearance.Remove.ERROR));
                        profile.setSkin(null);
                        profile.setName(null);
                    }
                });
                return true;
            }
            return false;
        });
    }
}
