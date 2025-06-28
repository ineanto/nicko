package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.Optional;

public class ResetItem {
    private final PlayerLanguage playerLanguage;

    public ResetItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.RESET);
        }, (event) -> {
            final Player player = event.getPlayer();
            final ClickType clickType = event.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                final Optional<NickoProfile> optionalProfile = NickoProfile.get(player);
                optionalProfile.ifPresent(profile -> {
                    if (!profile.hasData()) {
                        player.sendMessage(playerLanguage.translateWithOops(LanguageKey.Event.Appearance.Remove.MISSING));
                        event.getEvent().getView().close();
                        return;
                    }

                    final AppearanceManager appearanceManager = new AppearanceManager(player);
                    final ActionResult reset = appearanceManager.reset();
                    if (!reset.isError()) {
                        player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Remove.OK));
                    } else {
                        player.sendMessage(playerLanguage.translateWithOops(LanguageKey.Event.Appearance.Remove.ERROR, reset.getErrorKey()));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1f);
                    }
                });
                return true;
            }
            return false;
        });
    }
}
