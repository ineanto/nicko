package net.artelnatif.nicko.anvil;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangUtils;
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
                .onComplete((completion) -> {
                    if (MojangUtils.isUsernameInvalid(completion.getText())) {
                        return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                    } else {
                        appearanceManager.setName(completion.getText());
                        openSkinAnvil();
                        return Collections.singletonList(AnvilGUI.ResponseAction.close());
                    }
                })
                .text("New name...");
    }

    public AnvilGUI.Builder getNameAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onComplete((completion) -> {
                    if (MojangUtils.isUsernameInvalid(completion.getText())) {
                        return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                    } else {
                        appearanceManager.setName(completion.getText());
                        final ActionResult<Void> actionResult = appearanceManager.updatePlayer(false);
                        return sendResultAndClose(actionResult);
                    }
                })
                .text("New name...");
    }

    private AnvilGUI.Builder getSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(true))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onComplete((completion) -> {
                    if (MojangUtils.isUsernameInvalid(completion.getText())) {
                        return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                    } else {
                        appearanceManager.setSkin(completion.getText());
                        final ActionResult<Void> actionResult = appearanceManager.updatePlayer(true);
                        return sendResultAndClose(actionResult);
                    }
                })
                .text("New skin...");
    }

    private List<AnvilGUI.ResponseAction> sendResultAndClose(ActionResult<Void> actionResult) {
        if (!actionResult.isError()) {
            player.sendMessage(I18N.translate(player, I18NDict.Event.Disguise.SUCCESS));
        } else {
            player.sendMessage(I18N.translate(player, I18NDict.Event.Disguise.FAIL, I18N.translateWithoutPrefix(player, actionResult.getErrorMessage())));
        }
        return Collections.singletonList(AnvilGUI.ResponseAction.close());
    }

    private ItemStack getLeftItem(boolean skin) {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§0New " + (skin ? "skin" : "name") + "...");
        item.setItemMeta(meta);
        return item;
    }
}