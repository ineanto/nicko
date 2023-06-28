package xyz.atnrch.nicko.anvil;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.disguise.AppearanceManager;
import xyz.atnrch.nicko.disguise.ActionResult;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class AnvilManager {
    private final Player player;
    private final AppearanceManager appearanceManager;

    public AnvilManager(Player player) {
        this.player = player;
        this.appearanceManager = AppearanceManager.get(player);
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

    public AnvilGUI.Builder getNameThenSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            appearanceManager.setName(snapshot.getText());
                            openSkinAnvil();
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...");
    }

    public AnvilGUI.Builder getNameAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            appearanceManager.setName(snapshot.getText());
                            final ActionResult<Void> actionResult = appearanceManager.updatePlayer(false);
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
                            appearanceManager.setSkin(snapshot.getText());
                            final ActionResult<Void> actionResult = appearanceManager.updatePlayer(true);
                            return sendResultAndClose(actionResult);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New skin...");
    }

    private List<AnvilGUI.ResponseAction> sendResultAndClose(ActionResult<Void> actionResult) {
        final I18N i18n = new I18N(player);
        if (!actionResult.isError()) {
            player.sendMessage(i18n.translate(I18NDict.Event.Disguise.SUCCESS));
        } else {
            // TODO (Ineanto, 6/28/23): Check weirdness with error message not being translated sometimes
            player.sendMessage(i18n.translate(I18NDict.Event.Disguise.FAIL, i18n.translateWithoutPrefix(actionResult.getErrorKey())));
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