package xyz.atnrch.nicko.command.sub;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.disguise.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.mojang.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class NickoCheckSubCmd {
    public void execute(Player player, String[] args) {
        final String targetName = args[1];
        final Player target = Bukkit.getPlayerExact(targetName);

        AppearanceManager appearanceManager;
        if (MojangUtils.isUsernameInvalid(targetName)) {
            player.sendMessage(I18N.translate(player, I18NDict.Error.INVALID_USERNAME));
            return;
        }

        if (target == null) {
            appearanceManager = AppearanceManager.get(targetName);
        } else {
            appearanceManager = AppearanceManager.get(target);
        }

        final StringJoiner builder = new StringJoiner("\n");
        builder.add("§c" + NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§6Check for: §f§o" + targetName);
        if (!appearanceManager.hasData()) {
            builder.add("§cThis player has not data.");
        } else {
            builder.add("§7- §fNicked: §a✔");
            builder.add("§7- §fName: §6" + appearanceManager.getName());
            builder.add("§7- §fSkin: §6" + appearanceManager.getSkin());
        }

        player.sendMessage(builder.toString());
    }
}
