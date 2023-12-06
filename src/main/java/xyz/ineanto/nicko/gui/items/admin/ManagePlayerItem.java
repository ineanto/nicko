package xyz.ineanto.nicko.gui.items.admin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
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
            return i18n.translateItem(builder, I18NDict.GUI.Admin.MANAGE_PLAYER);
        }, click -> {
            new PlayerCheckGUI(player).open();
            return true;
        });
    }
}
