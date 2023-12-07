package xyz.ineanto.nicko.gui.items.admin.check;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.gui.ChoiceGUI;
import xyz.ineanto.nicko.gui.PlayerCheckGUI;
import xyz.ineanto.nicko.gui.items.ItemDefaults;
import xyz.ineanto.nicko.gui.items.common.choice.ChoiceCallback;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.profile.NickoProfile;
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

    public PlayerInformationItem(I18N i18n, Player target) {
        super(new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
            return i18n.translateItem(builder, I18NDict.GUI.LOADING);
        }, (click -> true)).getItemProvider(), () -> {
            try {
                final SkullBuilder skull = new SkullBuilder(target.getUniqueId());
                final Optional<NickoProfile> optionalProfile = NickoBukkit.getInstance().getDataStore().getData(target.getUniqueId());

                if (optionalProfile.isPresent()) {
                    final NickoProfile profile = optionalProfile.get();
                    return i18n.translateItem(skull, I18NDict.GUI.Admin.CHECK,
                            target.getName(),
                            (profile.hasData() ? "§a✔" : "§c❌"),
                            (profile.getName() == null ? "§7N/A" : profile.getName()),
                            (profile.getSkin() == null ? "§7N/A" : profile.getSkin()));
                }
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                NickoBukkit.getInstance().getLogger().severe("Unable to get head for specified UUID ( " + target.getUniqueId() + ")! (GUI/PlayerCheck)");
            }

            return ItemDefaults.getErrorSkullItem(i18n, I18NDict.GUI.Admin.CHECK,
                    "§c§l?!?", "§cN/A", "§cN/A", "§cN/A"
            );
        });
        this.target = target;
        this.profile = NickoBukkit.getInstance().getDataStore().getData(target.getUniqueId()).orElse(NickoProfile.EMPTY_PROFILE);
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
                        final I18N i18n = new I18N(player);
                        player.sendMessage(i18n.translate(I18NDict.Event.Admin.Check.REMOVE_SKIN, target.getName()));
                    }

                    @Override
                    public void onCancel() {
                        new PlayerCheckGUI(player).open();
                    }
                }).open();
            }
        }
    }

}
