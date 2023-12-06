package xyz.ineanto.nicko.gui.items.appearance;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.anvil.AnvilManager;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class ChangeSkinItem {
    private final I18N i18n;
    private final Player player;

    public ChangeSkinItem(Player player) {
        this.i18n = new I18N(player);
        this.player = player;
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            try {
                final SkullBuilder builder = new SkullBuilder(player.getName());
                return i18n.translateItem(builder, I18NDict.GUI.Home.CHANGE_SKIN);
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                NickoBukkit.getInstance().getLogger().warning("Unable to get Head texture for specified player (" + player.getName() + ")! (GUI/Home)");
                return ItemDefaults.getErrorSkullItem(i18n, I18NDict.GUI.Home.CHANGE_SKIN);
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
