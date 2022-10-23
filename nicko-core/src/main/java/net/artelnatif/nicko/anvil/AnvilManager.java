package net.artelnatif.nicko.anvil;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilManager {
    private final Player player;
    private final NickoProfile profile;
    private final AppearanceManager appearanceManager;

    public AnvilManager(Player player) {
        this.player = player;
        this.appearanceManager = AppearanceManager.get(player);
        this.profile = appearanceManager.getProfile();
    }

    public void openNameAndSkinAnvil() {
        final AnvilGUI.Builder skin = getSkinAnvil();
        final AnvilGUI.Builder name = new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem())
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        profile.setName(response);
                        skin.open(player);
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New name...");

        name.open(player);
    }

    public void openNameAnvil() {
        new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem())
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        appearanceManager.setName(response);
                        appearanceManager.updatePlayer(false);
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New name...")
                .open(player);
    }

    public void openSkinAnvil() {
        getSkinAnvil().open(player);
    }

    private AnvilGUI.Builder getSkinAnvil() {
        return new AnvilGUI.Builder()
                .plugin(NickoBukkit.getInstance())
                .itemLeft(getLeftItem())
                .onComplete((anvilPlayer, response) -> {
                    if (MojangUtils.isUsernameInvalid(response)) {
                        return AnvilGUI.Response.text("Invalid username!");
                    } else {
                        appearanceManager.setSkin(response);
                        appearanceManager.updatePlayer(true);
                        return AnvilGUI.Response.close();
                    }
                })
                .text("New skin...");
    }

    private ItemStack getLeftItem() {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§0ID");
        item.setItemMeta(meta);
        return item;
    }
}