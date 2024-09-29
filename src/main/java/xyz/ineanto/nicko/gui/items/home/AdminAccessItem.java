package xyz.ineanto.nicko.gui.items.home;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.AdminGUI;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class AdminAccessItem {
    private final PlayerLanguage playerLanguage;

    public AdminAccessItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.SCAFFOLDING);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.ADMIN);
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new AdminGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
