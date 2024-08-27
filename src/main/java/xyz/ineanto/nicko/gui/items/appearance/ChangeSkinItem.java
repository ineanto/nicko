package xyz.ineanto.nicko.gui.items.appearance;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.anvil.AnvilManager;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class ChangeSkinItem {
    private final PlayerLanguage playerLanguage;
    private final Player player;

    public ChangeSkinItem(Player player) {
        this.playerLanguage = new PlayerLanguage(player);
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            try {
                final SkullBuilder builder = new SkullBuilder(player.getName());
                return playerLanguage.translateItem(builder, LanguageKey.GUI.Home.CHANGE_SKIN);
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                Nicko.getInstance().getLogger().warning("Unable to get Head texture for specified player (" + player.getName() + ")! (GUI/Home)");
                return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Home.CHANGE_SKIN);
            }
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
