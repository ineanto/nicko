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
        final I18N i18n = new I18N(player);

        AppearanceManager appearanceManager;
        if (MojangUtils.isUsernameInvalid(targetName)) {
            player.sendMessage(i18n.translate(I18NDict.Error.INVALID_USERNAME));
            return;
        }

        if (target == null) {
            appearanceManager = AppearanceManager.get(targetName);
        } else {
            appearanceManager = AppearanceManager.get(target);
        }

        final StringJoiner builder = new StringJoiner("\n");
        builder.add("§c" + NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cCheck for: §f§o" + targetName);
        if (appearanceManager.hasData()) {
            builder.add("§cNicked: §a✔");
            builder.add("§cName: §6" + appearanceManager.getName());
            builder.add("§cSkin: §6" + appearanceManager.getSkin());
        } else {
            builder.add("§cNicked: §c❌");
            builder.add("§cName: §7N/A");
            builder.add("§cSkin: §7N/A");
        }
        player.sendMessage(builder.toString());
    }
}
