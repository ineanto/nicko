package net.artelnatif.nicko.command.sub;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class NickoCheckSubCmd extends NickoSubCmd {
    public NickoCheckSubCmd(NickoCommand nickoCommand) {
        super(nickoCommand);
    }

    public void execute(CommandSender sender, String[] args) {
        final String targetName = args[1];
        final Player target = Bukkit.getPlayerExact(targetName);

        AppearanceManager appearanceManager;
        if (PlayerUtils.isPlayerOffline(target)) {
            appearanceManager = AppearanceManager.get(targetName);
        } else {
            appearanceManager = AppearanceManager.get(target);
        }

        final StringJoiner builder = new StringJoiner("\n");
        builder.add("§c" + NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§f- §6Check for:§f§o" + targetName);
        if (!appearanceManager.hasData()) {
            builder.add("§cThis player has not data.");
        } else {
            builder.add("§7- §fNicked: " + (appearanceManager.isNicked() ? "§a✔" : "§c❌"));
            builder.add("§7- §fNickname: §6" + (appearanceManager.getName().equals(targetName) ? "N/A" : appearanceManager.getName()));
            builder.add("§7- §fSkin: §6" + (appearanceManager.getSkin().equals(targetName) ? "N/A" : appearanceManager.getSkin()));
        }

        sender.sendMessage(builder.toString());
    }
}
