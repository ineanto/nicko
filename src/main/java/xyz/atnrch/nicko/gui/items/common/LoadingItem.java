package xyz.atnrch.nicko.gui.items.common;

import org.bukkit.Material;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class LoadingItem {
    private final I18N i18n;

    public LoadingItem(I18N i18n) {
        this.i18n = i18n;
    }

    public ItemProvider get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.LOADING);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, (click -> true)).getItemProvider();
    }
}
