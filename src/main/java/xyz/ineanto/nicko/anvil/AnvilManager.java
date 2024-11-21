package xyz.ineanto.nicko.anvil;

import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.event.custom.PlayerDisguiseEvent;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.mojang.MojangUtils;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnvilManager {
    private final Player player;
    private final AppearanceManager appearanceManager;
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();
    private final NickoProfile profile;
    private final PlayerLanguage playerLanguage;

    public AnvilManager(Player player) {
        this.player = player;
        this.playerLanguage = new PlayerLanguage(player);

        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        this.profile = optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
        this.appearanceManager = new AppearanceManager(player);
    }

    public void openNameThenSkinAnvil() {
        getNameThenSkinAnvil().open(player);
    }

    public void openSkinAnvil() {
        getSkinAnvil().open(player);
    }

    public void openNameAnvil() {
        getNameAnvil().open(player);
    }

    private AnvilGUI.Builder getNameThenSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setName(snapshot.getText());
                            openSkinAnvil();
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...");
    }

    private AnvilGUI.Builder getNameAnvil() {
        return new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setName(snapshot.getText());
                            dataStore.updateCache(player.getUniqueId(), profile);
                            return sendResultAndClose(false);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...");
    }

    private AnvilGUI.Builder getSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(true))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setSkin(snapshot.getText());
                            dataStore.updateCache(player.getUniqueId(), profile);
                            return sendResultAndClose(true);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New skin...");
    }

    private List<AnvilGUI.ResponseAction> sendResultAndClose(boolean skinChange) {
        final PlayerDisguiseEvent event = new PlayerDisguiseEvent(player, profile.getSkin(), player.getName());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) { return Collections.singletonList(AnvilGUI.ResponseAction.close()); }

        final ActionResult actionResult = appearanceManager.update(skinChange, false);
        if (!actionResult.isError()) {
            player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Set.OK));
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1f);
        } else {
            player.sendMessage(
                    playerLanguage.translateWithOops(
                            LanguageKey.Event.Appearance.Set.ERROR,
                            playerLanguage.translate(actionResult.getErrorKey(), false)
                    ));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
        }
        return Collections.singletonList(AnvilGUI.ResponseAction.close());
    }

    private ItemStack getLeftItem(boolean skin) {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (skin) {
                meta.displayName(Component.text(playerLanguage.translate(LanguageKey.GUI.NEW_SKIN, false)));
            } else {
                meta.displayName(Component.text(playerLanguage.translate(LanguageKey.GUI.NEW_NAME, false)));
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}