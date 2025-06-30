package xyz.ineanto.nicko.gui.items.favorites;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class FavoriteAppearanceEntryItem extends AsyncItem {
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();

    private final PlayerLanguage playerLanguage;
    private final Appearance appearance;

    public FavoriteAppearanceEntryItem(PlayerLanguage playerLanguage, Appearance appearance) {
        super(new SuppliedItem(() -> {
                    final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
                    return playerLanguage.translateItem(builder, LanguageKey.GUI.LOADING);
                }, (_ -> true)).getItemProvider(),
                () -> {
                    try {
                        // TODO (Ineanto, 08/06/2025): set a default skin if the entry contains only a name
                        final String name = (appearance.name() == null ? "N/A" : appearance.name());
                        final String skin = (appearance.skin() == null ? "N/A" : appearance.skin());
                        final SkullBuilder skull = new SkullBuilder(skin);
                        return playerLanguage.translateItem(skull, LanguageKey.GUI.Favorites.ENTRY, name, skin);
                    } catch (Exception e) {
                        Nicko.getInstance().getLogger().warning("Unable to get Head texture for specified UUID (" + appearance.skin() + ")! (GUI/Favorites/Entry)");
                        return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Favorites.ENTRY, "N/A", "N/A");
                    }
                });
        this.playerLanguage = playerLanguage;
        this.appearance = appearance;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick() || clickType.isRightClick()) {
            event.getView().close();
            final NickoProfile profile = dataStore.getData(player.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);
            final AppearanceManager appearanceManager = new AppearanceManager(player);

            profile.setName(appearance.name());
            profile.setSkin(appearance.skin());
            dataStore.updateCache(player.getUniqueId(), profile);

            final ActionResult result = appearanceManager.update(true);
            if (!result.isError()) {
                player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Set.OK));

            } else {
                player.sendMessage(playerLanguage.translateWithOops(
                                LanguageKey.Event.Appearance.Set.ERROR,
                                result.getErrorKey()
                        )
                );
                appearanceManager.reset();
            }
        }
    }
}
