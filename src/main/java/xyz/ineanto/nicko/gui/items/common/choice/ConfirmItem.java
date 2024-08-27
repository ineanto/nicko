package xyz.ineanto.nicko.gui.items.common.choice;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ConfirmItem {
    private final PlayerLanguage playerLanguage;
    private final ChoiceCallback callback;

    public ConfirmItem(Player player, ChoiceCallback callback) {
        this.playerLanguage = new PlayerLanguage(player);
        this.callback = callback;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Choice.CONFIRM);
        }, click -> {
            click.getEvent().getView().close();
            callback.onConfirm();
            return true;
        });
    }
}
