package xyz.atnrch.nicko.command;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.ActionResult;
import xyz.atnrch.nicko.appearance.AppearanceManager;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.mojang.MojangUtils;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;

import java.util.Optional;

public class NickoDebugCmd {
    public void execute(CommandSender sender, String[] args) {
        final String prefix = NickoBukkit.getInstance().getNickoConfig().getPrefix();

        Player target;
        String name, skin;
        String playerName = null;
        if (args.length == 3) {
            target = (Player) sender;
            name = args[1];
            skin = args[2];
        } else {
            if (args.length < 3) {
                sender.sendMessage(prefix + "§cMissing argument.");
                return;
            }

            playerName = args[1];
            name = args[2];
            skin = args[3];

            target = Bukkit.getPlayer(playerName);
            if (target == null) {
                sender.sendMessage(prefix + "§cSpecified player is offline.");
                return;
            }
        }

        if (MojangUtils.isUsernameInvalid(name)) {
            sender.sendMessage(prefix + "§cSpecified username is invalid.");
            return;
        }

        if (MojangUtils.isUsernameInvalid(skin)) {
            sender.sendMessage(prefix + "§cSpecified skin is invalid.");
            return;
        }

        final Optional<NickoProfile> optionalProfile = NickoProfile.get(target);
        if (optionalProfile.isPresent()) {
            final NickoProfile profile = optionalProfile.get();
            final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
            profile.setName(name);
            profile.setSkin(skin);
            dataStore.updateCache(target.getUniqueId(), profile);
            final AppearanceManager appearanceManager = new AppearanceManager(target);
            final ActionResult result = appearanceManager.updatePlayer(true, false);
            if (!result.isError()) {
                target.sendMessage(prefix + "§aWhoosh!");
                target.playSound(target.getLocation(), Sound.ENTITY_ITEM_FRAME_PLACE, 1, 1);
            } else {
                final I18N i18n = new I18N(target);
                target.sendMessage(prefix + "§cWhoops. Something happened: " + i18n.translatePrefixless(result.getErrorKey()));
            }
        }
    }
}
