package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class RandomSkinItem {
    private final PlayerLanguage playerLanguage;
    private final Nicko instance;

    public RandomSkinItem(Player player) {
        this.instance = Nicko.getInstance();
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.SPAWNER);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.RANDOM_SKIN);
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
                    final ActionResult result = appearanceManager.updatePlayer(true, false);
                    if (!result.isError()) {
                        player.sendMessage(playerLanguage.translate(LanguageKey.Event.Appearance.Set.OK, true));
                    } else {
                        player.sendMessage(playerLanguage.translate(
                                        LanguageKey.Event.Appearance.Set.ERROR,
                                        true,
                                        playerLanguage.translate(result.getErrorKey(), false)
                                )
                        );
                    }
                });
                return true;
            }
            return false;
        });
    }
}
