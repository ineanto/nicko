package xyz.atnrch.nicko.command.sub;

import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.disguise.AppearanceManager;
import xyz.atnrch.nicko.mojang.MojangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickoDebugSubCmd {
    public void execute(CommandSender sender, String[] args) {
        final String prefix = NickoBukkit.getInstance().getNickoConfig().getPrefix();

        Player target;
        String name, skin;
        if (args.length == 3) {
            target = (Player) sender;
            name = args[1];
            skin = args[2];
        } else {
            if (args.length < 3) {
                sender.sendMessage(prefix + "§cMissing argument.");
                return;
            }

            final String playerName = args[1];
            name = args[2];
            skin = args[3];

            target = Bukkit.getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(prefix + "§cSpecified player is offline.");
                return;
            }
        }

        final AppearanceManager appearanceManager = AppearanceManager.get(target.getPlayer());

        if (MojangUtils.isUsernameInvalid(name) || MojangUtils.isUsernameInvalid(skin)) {
            sender.sendMessage(prefix + "§cSpecified username is invalid.");
        }

        appearanceManager.setNameAndSkin(name, skin);
        target.sendMessage(prefix + "§aWhoosh!");
        target.playSound(target.getLocation(), Sound.ENTITY_ITEM_FRAME_PLACE, 1, 1);
    }
}
