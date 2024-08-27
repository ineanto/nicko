package xyz.ineanto.nicko.gui.items.admin.check;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.gui.ChoiceGUI;
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;
import java.util.Optional;

public class PlayerInformationItem extends AsyncItem {
    private final Player target;
    private final NickoProfile profile;
    private final PlayerLanguage playerLanguage;

    public PlayerInformationItem(PlayerLanguage playerLanguage, Player target) {
        super(new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
            return playerLanguage.translateItem(builder, LanguageKey.GUI.LOADING);
        }, (click -> true)).getItemProvider(), () -> {
            try {
                final SkullBuilder skull = new SkullBuilder(target.getUniqueId());
                final Optional<NickoProfile> optionalProfile = Nicko.getInstance().getDataStore().getData(target.getUniqueId());

                if (optionalProfile.isPresent()) {
                    final NickoProfile profile = optionalProfile.get();
                    final AbstractItemBuilder<?> headItem = playerLanguage.translateItem(skull, LanguageKey.GUI.Admin.CHECK,
                            target.getName(),
                            (profile.hasData() ? "<green>✔</green>" : "<red>❌</red>"),
                            (profile.getName() == null ? "<grey>N/A<grey>" : profile.getName()),
                            (profile.getSkin() == null ? "<grey>N/A</grey>" : profile.getSkin()));

                    if (!profile.hasData()) {
                        // Remove the last 2 lines of the lore.
                        headItem.removeLoreLine(headItem.getLore().size() - 1);
                        headItem.removeLoreLine(headItem.getLore().size() - 1);
                    }

                    return headItem;
                }
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                Nicko.getInstance().getLogger().severe("Unable to get head for specified UUID ( " + target.getUniqueId() + ")! (GUI/PlayerCheck)");
            }

            return ItemDefaults.getErrorSkullItem(playerLanguage, LanguageKey.GUI.Admin.CHECK,
                    "§c§l?!?", "§7N/A", "§7N/A", "§7N/A"
            );
        });
        this.playerLanguage = playerLanguage;
        this.target = target;
        this.profile = Nicko.getInstance().getDataStore().getData(target.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);
    }

    @Override
    public void handleClick(@NotNull ClickType click, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (click.isLeftClick() || click.isRightClick()) {
            if (profile.hasData()) {
                event.getView().close();
                new ChoiceGUI(player, new ChoiceCallback() {
                    @Override
                    public void onConfirm() {
                        final AppearanceManager appearanceManager = new AppearanceManager(target);
                        appearanceManager.reset();
                        player.sendMessage(playerLanguage.translate(LanguageKey.Event.Admin.Check.REMOVE_SKIN, true, target.getName()));
                    }

                    @Override
                    public void onCancel() {
                        new PlayerCheckGUI(player, Bukkit.getOnlinePlayers()).open();
                    }
                }).open();
            }
        }
    }

}
