package xyz.ineanto.nicko.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import xyz.ineanto.nicko.gui.HomeGUI;

@SuppressWarnings("UnstableApiUsage")
public class NickoCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack stack, String[] strings) {
        final Entity executor = stack.getExecutor();
        final Player player = (Player) executor;

        new HomeGUI(player).open();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender instanceof Player && sender.isOp() || sender.hasPermission(permission());
    }

    @Override
    public @Nullable String permission() {
        return "nicko.use";
    }
}
