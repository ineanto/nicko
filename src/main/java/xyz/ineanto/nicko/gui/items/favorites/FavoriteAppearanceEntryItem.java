package xyz.ineanto.nicko.gui.items.favorites;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class FavoriteAppearanceEntryItem {
    private final AppearanceManager appearanceManager;
    private final PlayerLanguage playerLanguage;
    private final Appearance appearance;

    public FavoriteAppearanceEntryItem(Player player, Appearance appearance) {
        this.appearanceManager = new AppearanceManager(player);
        this.playerLanguage = new PlayerLanguage(player);
        this.appearance = appearance;
    }

    public AsyncItem get() {
        // TODO (Ineanto, 26/06/2025): handle click
        final ItemBuilder temporaryItemBuilder = new ItemBuilder(Material.PAINTING);
        return new AsyncItem(playerLanguage.translateItem(temporaryItemBuilder, LanguageKey.GUI.LOADING),
                () -> {
                    try {
                        // TODO (Ineanto, 08/06/2025): set a default skin if the entry contains only a name
                        final String name = (appearance.name() == null ? "N/A" : appearance.name());
                        final String skin = (appearance.skin() == null ? "N/A" : appearance.skin());
                        final SkullBuilder skull = new SkullBuilder(skin);
                        return playerLanguage.translateItem(skull, LanguageKey.GUI.Favorites.ENTRY, name, skin);
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        Nicko.getInstance().getLogger().warning("Unable to get Head texture for specified UUID (" + appearance.skin() + ")! (GUI/Favorites/Entry)");
                        return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Favorites.ENTRY, "N/A", "N/A");
                    }
                });
    }
}
