package xyz.atnrch.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.PlayerCheckGUI;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ManagePlayerItem {
    private final Player player;
    private final I18N i18n;

    public ManagePlayerItem(I18N i18n, Player player) {
        this.i18n = i18n;
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.MANAGE_PLAYER);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, click -> {
            new PlayerCheckGUI(player).open();
            return true;
        });
    }
}
