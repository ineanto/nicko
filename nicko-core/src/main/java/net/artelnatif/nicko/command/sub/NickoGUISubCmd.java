package net.artelnatif.nicko.command.sub;

import com.yoshiplex.rainbow.RainbowText;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.anvil.AnvilManager;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.command.NickoPermissions;
import net.artelnatif.nicko.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickoGUISubCmd extends NickoSubCmd {
    public NickoGUISubCmd(NickoCommand nickoCommand) {
        super(nickoCommand);
    }

    public void execute(CommandSender sender, String[] args) {
        Player target;
        if (args.length < 3) {
            target = (Player) sender;
        } else {
            final String playerName = args[1];
            target = Bukkit.getPlayerExact(playerName);
            if (PlayerUtils.isPlayerOffline(target)) {
                sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cSpecified player is offline.");
                return;
            }

            if (!sender.hasPermission(NickoPermissions.Player.Command.NICKO_PERMISSION_PLAYER_COMMAND_USE)) {
                sender.sendMessage(NickoPermissions.NICKO_PERMISSION_MISSING);
                return;
            }
        }

        final AnvilManager manager = new AnvilManager(target);
        final String arg = args.length < 3 ? args[1] : args[2];
        switch (arg) {
            case "name" -> manager.openNameAnvil();
            case "skin" -> manager.openSkinAnvil();
            case "full" -> manager.openNameAndSkinAnvil();
            default ->
                    sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cInvalid argument.");
        }

        final RainbowText whooshText = target == sender ? new RainbowText("Success!") : new RainbowText("Success! %player% is now undercover!".replace("%player%", target.getName()));
        sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + whooshText.getText());
    }
}