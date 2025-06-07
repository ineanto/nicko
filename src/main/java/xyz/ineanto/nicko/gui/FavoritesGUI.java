package xyz.ineanto.nicko.gui;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.gui.items.common.GoBackItem;
import xyz.ineanto.nicko.gui.items.common.ScrollDownItem;
import xyz.ineanto.nicko.gui.items.common.ScrollUpItem;
import xyz.ineanto.nicko.gui.items.favorites.FavoriteAppearanceEntryItem;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritesGUI {
    private final Player player;
    private final Gui gui;
    private final String title;

    public FavoritesGUI(Player player) {
        final PlayerLanguage playerLanguage = new PlayerLanguage(player);
        this.title = playerLanguage.translate(LanguageKey.GUI.Titles.FAVORITES, false);

        final HomeGUI parent = new HomeGUI(player);
        final GoBackItem backItem = new GoBackItem(player);
        final ScrollUpItem scrollUpItem = new ScrollUpItem(playerLanguage);
        final ScrollDownItem scrollDownItem = new ScrollDownItem(playerLanguage);

        final NickoProfile profile = Nicko.getInstance().getDataStore().getData(player.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);
        final List<Appearance> favorites = profile.getFavorites();
        List<Item> items;

        if (favorites == null || favorites.isEmpty()) {
            items = Collections.emptyList();
        } else {
            items = favorites.stream()
                    .map((appearance) -> new FavoriteAppearanceEntryItem(playerLanguage, appearance))
                    .collect(Collectors.toList());
        }

        gui = ScrollGui.items(guiItemBuilder -> {
            guiItemBuilder.setStructure(
                    "x x x x x x x x U",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x D",
                    "% % % A B R % % %");
            guiItemBuilder.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
            guiItemBuilder.addIngredient('U', scrollUpItem);
            guiItemBuilder.addIngredient('D', scrollDownItem);
            guiItemBuilder.addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()));
            guiItemBuilder.setContent(items);
        });
        this.player = player;
    }

    public Gui getGUI() {
        return gui;
    }

    public String getTitle() {
        return title;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(title).open(player);
    }
}
