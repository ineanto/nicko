package xyz.ineanto.nicko.gui.items.admin.cache;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.InvalidateSkinGUI;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class InvalidateSkinItem {
    private final PlayerLanguage playerLanguage;

    public InvalidateSkinItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAPER);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Admin.Cache.INVALIDATE_SKIN);
        }, (click) -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new InvalidateSkinGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
