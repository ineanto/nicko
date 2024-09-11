package xyz.ineanto.nicko.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.ineanto.nicko.gui.HomeGUI;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;

public class NickoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.isOp() || player.hasPermission("nicko.use") || player.hasPermission("nicko.*")) {
                new HomeGUI(player).open();
            } else {
                final PlayerLanguage playerLanguage = new PlayerLanguage(player);
                player.sendMessage(playerLanguage.translate(LanguageKey.Error.PERMISSION, true));
            }
            return false;
        }

        sender.sendMessage("This plugin can only be used in-game. Sorry!");
        return false;
    }
}
