package xyz.atnrch.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.appearance.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class ResetItem {
    private final I18N i18n;

    public ResetItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.RESET);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, (event) -> {
            final Player player = event.getPlayer();
            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
                final AtomicBoolean result = new AtomicBoolean(false);
                optionalProfile.ifPresent(profile -> {
                    if (!profile.hasData()) {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.MISSING));
                        event.getEvent().getView().close();
                        result.set(true);
                        return;
                    }

                    final AppearanceManager appearanceManager = new AppearanceManager(player);
                    if (!appearanceManager.reset().isError()) {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.OK));
                        result.set(false);
                    } else {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Remove.ERROR));
                        result.set(true);
                    }
                });
                return result.get();
            }
            return false;
        });
    }
}
