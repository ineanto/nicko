package xyz.atnrch.nicko.gui.items.home;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.SettingsGUI;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class SettingsAccessItem {
    private final I18N i18n;

    public SettingsAccessItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMPARATOR);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.SETTINGS);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new SettingsGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
