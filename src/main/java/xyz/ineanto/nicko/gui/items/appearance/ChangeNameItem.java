package xyz.ineanto.nicko.gui.items.appearance;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.gui.prompt.PromptManager;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class ChangeNameItem {
    private final PlayerLanguage playerLanguage;

    public ChangeNameItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.NAME_TAG);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.CHANGE_NAME);
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final PromptManager manager = new PromptManager(click.getPlayer());
                manager.displayNamePromptThenUpdate();
            }
            return true;
        });
    }
}
