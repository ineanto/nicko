package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.FavoritesGUI;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class FavoritesItem {
    private final PlayerLanguage playerLanguage;

    public FavoritesItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.CHEST);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.FAVORITES);
        }, click -> {
            final Player player = click.getPlayer();
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new FavoritesGUI(click.getPlayer()).open();
                player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1f);
                return true;
            }
            return false;
        });
    }
}