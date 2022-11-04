package net.artelnatif.nicko.command.sub;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.i18n.I18N;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.artelnatif.nicko.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class NickoCheckSubCmd extends NickoSubCmd {
    public NickoCheckSubCmd(NickoCommand nickoCommand) {
        super(nickoCommand);
    }

    public void execute(Player player, String[] args) {
        final String targetName = args[1];
        final Player target = Bukkit.getPlayerExact(targetName);

        AppearanceManager appearanceManager;
        if (MojangUtils.isUsernameInvalid(targetName)) {
            player.sendMessage(I18N.translate(player, I18NDict.Error.INVALID_USERNAME));
            return;
        }

        if (PlayerUtils.isPlayerOffline(target)) {
            appearanceManager = AppearanceManager.get(targetName);
        } else {
            appearanceManager = AppearanceManager.get(target);
        }

        final StringJoiner builder = new StringJoiner("\n");
        builder.add("§c" + NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§6Check for: §f§o" + targetName);
        if (!appearanceManager.hasData()) {
            builder.add("§cThis player has not data.");
        } else {
            builder.add("§7- §fNicked: " + (appearanceManager.hasData() ? "§a✔" : "§c❌"));
            builder.add("§7- §fName: §6" + appearanceManager.getName());
            builder.add("§7- §fSkin: §6" + appearanceManager.getSkin());
        }

        player.sendMessage(builder.toString());
    }
}
