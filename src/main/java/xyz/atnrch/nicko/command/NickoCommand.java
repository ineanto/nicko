package xyz.atnrch.nicko.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.gui.HomeGUI;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;

public class NickoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
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
}
