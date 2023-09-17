package xyz.atnrch.nicko.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.HomeGUI;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;

public class NickoCommand implements CommandExecutor {
    private String helpMessage = "§cNicko §8§o[{version}] §f- §2Help:\n" +
                                 "§6/nicko §f- §7Open the GUI.\n" +
                                 "§6/nicko help §f- §7Print this help message.\n";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length >= 1) {
                if (args[0].equals("debug")) {
                    new NickoDebugCmd().execute(player, args);
                } else {
                    sendHelpMessage(sender);
                }
                return false;
            }

            if (player.isOp() || player.hasPermission("nicko.use") || player.hasPermission("nicko.*")) {
                new HomeGUI(player).open();
            } else {
                final I18N i18N = new I18N(player);
                player.sendMessage(i18N.translate(I18NDict.Error.PERMISSION));
            }
            return false;
        }

        sender.sendMessage("This plugin can only be used in-game. Sorry!");
        return false;
    }

    public void sendHelpMessage(CommandSender sender) {
        helpMessage = helpMessage.replace("{version}", NickoBukkit.getInstance().getPluginMeta().getVersion());
        sender.sendMessage(helpMessage);
    }
}
