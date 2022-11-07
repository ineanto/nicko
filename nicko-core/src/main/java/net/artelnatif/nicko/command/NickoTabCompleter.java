package net.artelnatif.nicko.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class NickoTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                return List.of("help", "check");
            } else if (args.length == 2 && !args[0].equalsIgnoreCase("help")) {
                return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
            } else {
                return Collections.emptyList();
            }
        }
        return null;
    }
}
