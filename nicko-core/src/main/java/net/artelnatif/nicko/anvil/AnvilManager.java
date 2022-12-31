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
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        appearanceManager.setName(response);
                        openSkinAnvil();
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New name...");
    }

    public AnvilGUI.Builder getNameAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(false))
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        appearanceManager.setName(response);
                        final ActionResult actionResult = appearanceManager.updatePlayer(false);
                        if (!actionResult.isError()) {
                            player.sendMessage(I18N.translate(player, I18NDict.Event.DISGUISE_SUCCESS));
                        } else {
                            player.sendMessage(I18N.translate(player, I18NDict.Event.DISGUISE_FAIL, I18N.translateFlat(player, actionResult.getErrorMessage())));
                        }
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New name...");
    }

    private AnvilGUI.Builder getSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem(true))
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        appearanceManager.setSkin(response);
                        final ActionResult actionResult = appearanceManager.updatePlayer(true);
                        if (!actionResult.isError()) {
                            player.sendMessage(I18N.translate(player, I18NDict.Event.DISGUISE_SUCCESS));
                        } else {
                            player.sendMessage(I18N.translate(player, I18NDict.Event.DISGUISE_FAIL, I18N.translateFlat(player, actionResult.getErrorMessage())));
                        }
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New skin...");
    }

    private ItemStack getLeftItem(boolean skin) {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§0New " + (skin ? "skin" : "name") + "...");
        item.setItemMeta(meta);
        return item;
    }
}