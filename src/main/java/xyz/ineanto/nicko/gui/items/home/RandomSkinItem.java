package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class RandomSkinItem {
    private final I18N i18n;
    private final NickoBukkit instance;

    public RandomSkinItem(Player player) {
        this.instance = NickoBukkit.getInstance();
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final SkullBuilder.HeadTexture texture = new SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzMTEzOGMyMDYxMWQzMDJjNDIzZmEzMjM3MWE3NDNkMTc0MzdhMTg5NzNjMzUxOTczNDQ3MGE3YWJiNCJ9fX0=");
            final SkullBuilder builder = new SkullBuilder(texture);
            return i18n.translateItem(builder, I18NDict.GUI.Home.RANDOM_SKIN);
        }, (event) -> {
            final Player player = event.getPlayer();
            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
                optionalProfile.ifPresent(profile -> {
                    final String name = instance.getNameFetcher().getRandomUsername();
                    final String skin = instance.getNameFetcher().getRandomUsername();
                    profile.setName(name);
                    profile.setSkin(skin);
                    instance.getDataStore().updateCache(player.getUniqueId(), profile);

                    final AppearanceManager appearanceManager = new AppearanceManager(player);
                    if (!appearanceManager.updatePlayer(true, false).isError()) {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Set.OK, true));
                    } else {
                        player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Set.ERROR, true));
                    }
                });
                return true;
            }
            return false;
        });
    }
}
