package net.artelnatif.nicko.command;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.sub.NickoCheckSubCmd;
import net.artelnatif.nicko.command.sub.NickoDebugSubCmd;
import net.artelnatif.nicko.gui.MainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickoCommand implements CommandExecutor {
    private String helpMessage = """
            §cNicko §8§o[{version}] §f- §2Help:
            §6/nicko §f- §7Open the GUI.
            §6/nicko help §f- §7Print this help message.
            """;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                switch (args[0]) {
                    case "debug" -> new NickoDebugSubCmd().execute(sender, args);
                    case "check" -> new NickoCheckSubCmd().execute(player, args);
                    default -> sendHelpMessage(sender);
                }
                return false;
            }

            new MainGUI(player).open();
            return false;
        }

        sender.sendMessage("This plugin can only be used in-game. Sorry!");
        return false;
    }

    public void sendHelpMessage(CommandSender sender) {
        helpMessage = helpMessage.replace("{version}", NickoBukkit.getInstance().getDescription().getVersion());
        sender.sendMessage(helpMessage);
    }
}
