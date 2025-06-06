package xyz.ineanto.nicko.gui.items.favorites;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.Appearance;
import xyz.ineanto.nicko.gui.ChoiceGUI;
import xyz.ineanto.nicko.gui.InvalidateSkinGUI;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.mojang.MojangAPI;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;

public class FavoriteAppearanceEntryItem extends AsyncItem {
    private final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();
    private final PlayerLanguage playerLanguage;
    private final Appearance appearance;

    public FavoriteAppearanceEntryItem(PlayerLanguage playerLanguage, Appearance appearance) {
        super(new SuppliedItem(() -> {
                    final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
                    return playerLanguage.translateItem(builder, LanguageKey.GUI.LOADING);
                }, (_ -> true)).getItemProvider(),
                () -> {
                    try {
                        final String name = (appearance.name() == null ? appearance.skin() : appearance.name());
                        final String skin = (appearance.skin() == null ? appearance.name() : appearance.skin());
                        final SkullBuilder skull = new SkullBuilder(skin);
                        return playerLanguage.translateItem(skull, LanguageKey.GUI.Admin.Cache.ENTRY, name);
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        Nicko.getInstance().getLogger().warning("Unable to get Head texture for specified UUID (" + appearance.skin() + ")! (GUI/Favorites/Entry)");
                        return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Admin.Cache.ENTRY, Nicko.getInstance().getMojangAPI().getUUIDName("Notch"));
                    }
                });
        this.playerLanguage = playerLanguage;
        this.appearance = appearance;
    }

    @Override
    public void handleClick(@NotNull ClickType click, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (click.isLeftClick() || click.isRightClick()) {
            event.getView().close();
            new ChoiceGUI(player, new ChoiceCallback() {
                @Override
                public void onConfirm() {
                    player.sendMessage(playerLanguage.translate(LanguageKey.Event.Admin.Cache.INVALIDATE_ENTRY, true, appearance.name()));
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1f);
                }

                @Override
                public void onCancel() {
                    new InvalidateSkinGUI(player).open();
                }
            }).open();
        }
    }
}
