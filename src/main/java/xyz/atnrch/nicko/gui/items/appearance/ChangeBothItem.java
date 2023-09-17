package xyz.atnrch.nicko.gui.items.appearance;

import org.bukkit.entity.Player;
import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeBothItem {
    private final I18N i18n;

    public ChangeBothItem(Player player) {
        this.i18n = new I18N(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.TOTEM_OF_UNDYING);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.CHANGE_BOTH);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final AnvilManager manager = new AnvilManager(click.getPlayer());
                manager.openNameThenSkinAnvil();
            }
            return true;
        });
    }
}
