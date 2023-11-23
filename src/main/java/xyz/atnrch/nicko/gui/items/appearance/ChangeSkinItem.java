package xyz.atnrch.nicko.gui.items.appearance;

import org.bukkit.Material;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.anvil.AnvilManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
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
                final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.CHANGE_SKIN);
                builder.setDisplayName(translation.getName());
                translation.getLore().forEach(builder::addLoreLines);
                return builder;
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                final ItemBuilder builder = new ItemBuilder(Material.TNT);
                final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.ERROR);
                builder.setDisplayName(translation.getName());
                translation.getLore().forEach(builder::addLoreLines);
                NickoBukkit.getInstance().getLogger().warning("Unable to get Head texture for specified player (" + player.getName() + ")! (GUI/Home)");
                return builder;
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
