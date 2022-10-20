package net.artelnatif.nicko.command.sub;

import com.yoshiplex.rainbow.RainbowText;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.command.NickoCommand;
import net.artelnatif.nicko.disguise.AppearanceManager;
import net.artelnatif.nicko.mojang.MojangUtils;
import net.artelnatif.nicko.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickoDebugSubCmd extends NickoSubCmd {

    public NickoDebugSubCmd(NickoCommand nickoCommand) {
        super(nickoCommand);
    }

    public void execute(CommandSender sender, String[] args) {
        Player target;
        String name, skin;
        if (args.length == 3) {
            target = (Player) sender;
            name = args[1];
            skin = args[2];
        } else {
            final String playerName = args[1];
            target = Bukkit.getPlayer(playerName);

            if(args.length < 3) {
                sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cMissing argument.");
            }

            name = args[2];
            skin = args[3];
            if (PlayerUtils.isPlayerOffline(target)) {
                sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cSpecified player is offline.");
                return;
            }
        }

        final AppearanceManager appearanceManager = AppearanceManager.get(target.getPlayer());

        if (MojangUtils.isUsernameInvalid(name) || MojangUtils.isUsernameInvalid(skin)) {
            sender.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cSpecified username is invalid.");
        }

        appearanceManager.setNameAndSkin(name, skin);
        final RainbowText whooshText = new RainbowText("Whoosh!");
        target.sendMessage(NickoBukkit.getInstance().getNickoConfig().getPrefix() + whooshText.getText());
        target.playSound(target.getLocation(), Sound.ENTITY_ITEM_FRAME_PLACE, 1, 1);
    }
}
