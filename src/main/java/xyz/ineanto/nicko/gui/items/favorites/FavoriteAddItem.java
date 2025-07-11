package xyz.ineanto.nicko.gui.items.favorites;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.gui.FavoritesGUI;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

import java.util.List;

public class FavoriteAddItem {
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();

    private final Player player;
    private final PlayerLanguage playerLanguage;
    private final NickoProfile profile;

    public FavoriteAddItem(Player player) {
        this.player = player;
        this.playerLanguage = new PlayerLanguage(player);
        this.profile = dataStore.getData(player.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);
    }

    public SuppliedItem get() {
        return new SuppliedItem(() -> {
            final ItemStack banner = new ItemStack(Material.GREEN_BANNER);
            final BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();

            // Plus sign
            bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
            bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));

            // Remove excess on the borders
            bannerMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.BORDER));
            bannerMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.CURLY_BORDER));
            bannerMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.STRIPE_TOP));
            bannerMeta.addPattern(new Pattern(DyeColor.GREEN, PatternType.STRIPE_BOTTOM));

            banner.setItemMeta(bannerMeta);

            banner.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
            banner.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay
                    .tooltipDisplay()
                    .addHiddenComponents(DataComponentTypes.BANNER_PATTERNS)
                    .build()
            );

            final ItemBuilder builder = new ItemBuilder(banner);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.Favorites.ADD);
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isShiftClick() && clickType.isLeftClick()) {
                if (!profile.hasData()) {
                    click.getEvent().getView().close();
                    return false;
                }

                final List<Appearance> favorites = profile.getFavorites();
                favorites.add(profile.getAppearance());
                dataStore.updateCache(player.getUniqueId(), profile);
                new FavoritesGUI(player).open();
                return true;
            }
            return false;
        });
    }
}
