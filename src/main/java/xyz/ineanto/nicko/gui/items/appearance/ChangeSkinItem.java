package xyz.ineanto.nicko.gui.items.appearance;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.gui.prompt.PromptManager;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class ChangeSkinItem {
    private final AppearanceManager appearanceManager;
    private final PlayerLanguage playerLanguage;
    private final PlayerNameStore playerNameStore;
    private final Player player;

    public ChangeSkinItem(Player player) {
        this.appearanceManager = new AppearanceManager(player);
        this.playerLanguage = new PlayerLanguage(player);
        this.playerNameStore = Nicko.getInstance().getNameStore();
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            AbstractItemBuilder<?> builder;

            try {
                builder = new SkullBuilder(playerNameStore.getStoredName(player));
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                builder = new ItemBuilder(Material.PLAYER_HEAD);
            }

            return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.CHANGE_SKIN);
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                final PromptManager manager = new PromptManager(click.getPlayer());
                manager.displaySkinPromptThenUpdate();
            }
            return true;
        });
    }
}
