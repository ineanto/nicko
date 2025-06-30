package xyz.ineanto.nicko.gui.items.favorites;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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

public class FavoriteAppearanceEntryItem {
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();

    private final AppearanceManager appearanceManager;
    private final PlayerLanguage playerLanguage;
    private final Appearance appearance;
    private final Player player;

    public FavoriteAppearanceEntryItem(Player player, Appearance appearance) {
        this.player = player;
        this.appearanceManager = new AppearanceManager(player);
        this.playerLanguage = new PlayerLanguage(player);
        this.appearance = appearance;
    }

    public AsyncItem get() {
        // what the f is this entanglement of suppliers
        return new AsyncItem(playerLanguage.translateItem(new ItemBuilder(Material.PAINTING), LanguageKey.GUI.LOADING),
                () -> new SuppliedItem(() -> {
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
                }, (click) -> {
                    System.out.println("there's a click folks!");
                    final ClickType clickType = click.getClickType();

                    if (clickType.isLeftClick() || clickType.isRightClick()) {
                        click.getEvent().getView().close();
                        final NickoProfile profile = dataStore.getData(player.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);

                        profile.setName(appearance.name());
                        profile.setSkin(appearance.skin());
                        dataStore.updateCache(player.getUniqueId(), profile);

                        final ActionResult result = appearanceManager.update(true);
                        if (!result.isError()) {
                            player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Set.OK));
                            return true;
                        } else {
                            player.sendMessage(playerLanguage.translateWithOops(
                                            LanguageKey.Event.Appearance.Set.ERROR,
                                            result.getErrorKey()
                                    )
                            );
                            appearanceManager.reset();
                            return false;
                        }
                    }
                    return false;
                }).getItemProvider()
        );
    }
}
