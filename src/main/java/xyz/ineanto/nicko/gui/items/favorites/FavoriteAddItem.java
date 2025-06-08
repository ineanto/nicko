package xyz.ineanto.nicko.gui.items.favorites;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class FavoriteAddItem {
    private final PlayerLanguage playerLanguage;

    public FavoriteAddItem(PlayerLanguage playerLanguage) {
        this.playerLanguage = playerLanguage;
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
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                return true;
            }
            return false;
        });
    }
}
