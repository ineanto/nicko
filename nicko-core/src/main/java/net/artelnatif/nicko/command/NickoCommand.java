package net.artelnatif.nicko.command;

import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.sub.NickoCheckSubCmd;
import net.artelnatif.nicko.command.sub.NickoDebugSubCmd;
import net.artelnatif.nicko.gui.MainGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NickoCommand implements CommandExecutor {
    private final String opPrefix = "§c(OP)";
    private String helpMessage = """
            §cNicko §8§o[{version}] §f- §2Help:
            §6/nicko §f- §7Open the GUI.
            §6/nicko help §f- §7Print this help message.
            """;
    private String adminHelpMessage = """
            {admin} §6/nicko disguise §b<player> §f- §7Change specified player's appearance.
            {admin} §6/nicko revert §b<player> §f- §7Revert specified player's appearance to their default skin and name.
            {admin} §6/nicko check §b<player> §f- §7Print detailed information about specified player's appearance.
            """.replace("{admin}", opPrefix);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                switch (args[0]) {
                    case "debug" -> new NickoDebugSubCmd(this).execute(sender, args);
                    case "check" -> new NickoCheckSubCmd(this).execute(player, args);
                    default -> sendHelpMessages(sender);
                }
                return false;
            }

            new MainGUI(player).open();
            return false;
        }

        sender.sendMessage("The GUI can only be opened in-game.");
        return false;
    }

    public void sendHelpMessages(CommandSender sender) {
        helpMessage = helpMessage.replace("{version}", NickoBukkit.getInstance().getDescription().getVersion());
        if (sender instanceof ConsoleCommandSender) {
            helpMessage = ChatColor.stripColor(helpMessage);
            adminHelpMessage = ChatColor.stripColor(adminHelpMessage);
        }
        sender.sendMessage(helpMessage);
        if (sender.isOp()) {
            sender.sendMessage(adminHelpMessage);
        }
    }
}
