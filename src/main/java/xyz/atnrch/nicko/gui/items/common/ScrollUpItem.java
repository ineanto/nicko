package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem;

public class ScrollUpItem extends ScrollItem {
    final I18N i18n;

    public ScrollUpItem(I18N i18n) {
        super(-1);
        this.i18n = i18n;
    }

    @Override
    public ItemProvider getItemProvider(ScrollGui gui) {
        final ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE);
        final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.SCROLL_UP);
        builder.setDisplayName(translation.getName());
        if (!gui.canScroll(-1)) translation.getLore().forEach(builder::addLoreLines);
        return builder;
    }

}

