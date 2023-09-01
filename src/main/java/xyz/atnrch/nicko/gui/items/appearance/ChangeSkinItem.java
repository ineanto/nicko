package xyz.atnrch.nicko.gui.items.appearance;

import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeSkinItem {
    private final I18N i18n;
    private final Player player;

    public ChangeSkinItem(Player player) {
        this.i18n = new I18N(player);
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final SkullBuilder builder = new SkullBuilder(player.getName());
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.CHANGE_SKIN);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final AnvilManager manager = new AnvilManager(click.getPlayer());
                manager.openSkinAnvil();
            }
            return true;
        });
    }
}
