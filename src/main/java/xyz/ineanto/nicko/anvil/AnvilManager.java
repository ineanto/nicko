package xyz.ineanto.nicko.anvil;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.mojang.MojangUtils;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AnvilManager {
    private final Player player;
    private final AppearanceManager appearanceManager;
    private final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
    private final NickoProfile profile;

    public AnvilManager(Player player) {
        this.player = player;

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
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setName(snapshot.getText());
                            dataStore.updateCache(player.getUniqueId(), profile);
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
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setName(snapshot.getText());
                            dataStore.updateCache(player.getUniqueId(), profile);
                            final ActionResult actionResult = appearanceManager.updatePlayer(false, false);
                            return sendResultAndClose(actionResult);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...");
    }

    private AnvilGUI.Builder getSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(true))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            profile.setSkin(snapshot.getText());
                            dataStore.updateCache(player.getUniqueId(), profile);
                            final ActionResult actionResult = appearanceManager.updatePlayer(true, false);
                            return sendResultAndClose(actionResult);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New skin...");
    }

    private List<AnvilGUI.ResponseAction> sendResultAndClose(ActionResult actionResult) {
        final I18N i18n = new I18N(player);
        if (!actionResult.isError()) {
            player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Set.OK));
        } else {
            player.sendMessage(i18n.translate(I18NDict.Event.Appearance.Set.ERROR, i18n.translatePrefixless(actionResult.getErrorKey())));
        }
        return Collections.singletonList(AnvilGUI.ResponseAction.close());
    }

    private ItemStack getLeftItem(boolean skin) {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) meta.setDisplayName("§0New " + (skin ? "skin" : "name") + "...");
        item.setItemMeta(meta);
        return item;
    }
}