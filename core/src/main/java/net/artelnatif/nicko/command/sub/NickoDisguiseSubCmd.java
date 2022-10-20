package net.artelnatif.nicko.command.sub;

import com.yoshiplex.rainbow.RainbowText;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.NickoCommand;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import xyz.upperlevel.spigot.book.BookUtil;

import java.util.Random;

public class NickoDisguiseSubCmd extends NickoSubCmd {

    public NickoDisguiseSubCmd(NickoCommand nickoCommand) {
        super(nickoCommand);
    }

    public void execute(CommandSender sender) {
        if (sender instanceof Player player) {
            final String disguiseBaseCommand = "/nicko gui {player} ";
            final String disguiseNameCommand = disguiseBaseCommand + "name";
            final String disguiseSkinCommand = disguiseBaseCommand + "skin";
            final String disguiseBothCommand = disguiseBaseCommand + "full";

            final RainbowText text = new RainbowText(NickoBukkit.getInstance().getNickoConfig().getDisguiseKitHeader());
            final int displacement = new Random().nextInt(RainbowText.getDefaultRainbow().size() - 1);
            text.setPlace(displacement);

            final ItemStack book = BookUtil.writtenBook()
                    .author("Nicko")
                    .title("§6Nicko - Book of magic")
                    .generation(BookMeta.Generation.ORIGINAL)
                    .pages(
                            new BookUtil.PageBuilder()
                                    .add(
                                            new TextComponent(text.getText())
                                    )
                                    .newLine()
                                    .newLine()
                                    .add("§0This disguise kit will help you change your name and skin.")
                                    .newLine()
                                    .newLine()
                                    .add("§0Go through each page until the end or cancel the process at any time by exiting any of the interfaces.")
                                    .build(),
                            new BookUtil.PageBuilder()
                                    .add(
                                            BookUtil.TextBuilder.of("§6> §0Change my skin")
                                                    .onHover(BookUtil.HoverAction.showText("Clicking this will prompt you a menu to change your skin."))
                                                    .onClick(BookUtil.ClickAction.runCommand(disguiseSkinCommand.replace("{player}", player.getName())))
                                                    .build()
                                    )
                                    .newLine()
                                    .newLine()
                                    .add(
                                            BookUtil.TextBuilder.of("§6> §0Change my name")
                                                    .onHover(BookUtil.HoverAction.showText("Clicking this will prompt you a menu to change your name."))
                                                    .onClick(BookUtil.ClickAction.runCommand(disguiseNameCommand.replace("{player}", player.getName())))
                                                    .build()
                                    )
                                    .newLine()
                                    .newLine()
                                    .add(
                                            BookUtil.TextBuilder.of("§6> §0Change both")
                                                    .onHover(BookUtil.HoverAction.showText("Clicking this will prompt you a menu to change both your name and your skin."))
                                                    .onClick(BookUtil.ClickAction.runCommand(disguiseBothCommand.replace("{player}", player.getName())))
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            BookUtil.openPlayer(player, book);
        } else {
            sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "This command can only be performed by players!");
        }
    }
}
