package xyz.ineanto.nicko.gui.items.admin.cache;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.Nicko;
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
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;
import java.util.UUID;

public class CacheEntryItem extends AsyncItem {
    private final String name;
    private final String uuid;
    private final MojangAPI mojangAPI = Nicko.getInstance().getMojangAPI();

    public CacheEntryItem(PlayerLanguage playerLanguage, String uuid) {
        super(new ItemBuilder(Material.PAINTING)
                        .setDisplayName(
                                Component.text(playerLanguage.translate(LanguageKey.GUI.LOADING, false)).content()
                        ),
                () -> {
                    final String dashedUuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
                    final UUID uuidObject = UUID.fromString(dashedUuid);
                    try {
                        final SkullBuilder skull = new SkullBuilder(uuidObject);
                        return playerLanguage.translateItem(skull, LanguageKey.GUI.Admin.Cache.ENTRY, Nicko.getInstance().getMojangAPI().getUUIDName(uuid));
                    } catch (MojangApiUtils.MojangApiException | IOException e) {
                        Nicko.getInstance().getLogger().warning("Unable to get Head texture for specified UUID (" + uuid + ")! (GUI/Cache/Entry)");
                        return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Admin.Cache.ENTRY, Nicko.getInstance().getMojangAPI().getUUIDName(uuid));
                    }
                });
        this.uuid = uuid;
        this.name = mojangAPI.getUUIDName(uuid);
    }

    @Override
    public void handleClick(@NotNull ClickType click, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (click.isLeftClick() || click.isRightClick()) {
            event.getView().close();
            new ChoiceGUI(player, new ChoiceCallback() {
                @Override
                public void onConfirm() {
                    final PlayerLanguage playerLanguage = new PlayerLanguage(player);
                    player.sendMessage(playerLanguage.translate(LanguageKey.Event.Admin.Cache.INVALIDATE_ENTRY, true, name));
                    player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1f);
                    mojangAPI.eraseFromCache(uuid);
                }

                @Override
                public void onCancel() {
                    new InvalidateSkinGUI(player).open();
                }
            }).open();
        }
    }
}
