package xyz.ineanto.nicko.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.gui.HomeGUI;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class NickoCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack stack, String[] args) {
        final Entity executor = stack.getExecutor();
        final Player player = (Player) executor;
        final PlayerLanguage playerLanguage = new PlayerLanguage(player);

        if (player == null) { return; }

        if (args.length >= 1 && args[0].equalsIgnoreCase("about")) {
            final Component firstAboutMessage = MiniMessage.miniMessage().deserialize(
                    "<prefix> <gray>v<version></gray>",
                    Placeholder.component("prefix", playerLanguage.getPrefixComponent()),
                    Placeholder.unparsed("version", Nicko.getInstance().getPluginMeta().getVersion())
            );

            final Component secondAboutMessage = MiniMessage.miniMessage().deserialize(
                    "<gradient:#01a97c:#8ffd54>Configuration</gradient> <gray>v<configversion></gray>, <gradient:#01a97c:#8ffd54>I18N</gradient> <gray>v<i18nversion></gray>",
                    Placeholder.component("prefix", playerLanguage.getPrefixComponent()),
                    Placeholder.unparsed("configversion", Configuration.VERSION.toString()),
                    Placeholder.unparsed("i18nversion", Language.VERSION.toString())

            );

            player.sendMessage(firstAboutMessage);
            player.sendMessage(secondAboutMessage);

            return;
        }

        new HomeGUI(player).open();
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender instanceof Player && sender.isOp() || sender.hasPermission(permission());
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return List.of("about");
    }

    @Override
    public @Nullable String permission() {
        return "nicko.use";
    }
}
