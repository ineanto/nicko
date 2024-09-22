package xyz.ineanto.nicko.gui.items.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ManagePlayerItem {
    private final Player player;
    private final PlayerLanguage playerLanguage;

    public ManagePlayerItem(PlayerLanguage playerLanguage, Player player) {
        this.playerLanguage = playerLanguage;
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.WRITABLE_BOOK);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Admin.MANAGE_PLAYER);
        }, click -> {
            new PlayerCheckGUI(player, Bukkit.getOnlinePlayers()).open();
            return true;
        });
    }
}
