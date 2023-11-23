package xyz.atnrch.nicko.gui.items.admin.check;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.util.MojangApiUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class PlayerInformationItem extends AsyncItem {
    public PlayerInformationItem(I18N i18n, UUID uuid) {
        super(new SuppliedItem(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.PAINTING);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.LOADING);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        }, (click -> true)).getItemProvider(), () -> {
            final Player player = Bukkit.getPlayer(uuid);
            try {
                final SkullBuilder skull = new SkullBuilder(uuid);
                final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
                final Optional<NickoProfile> optionalProfile = dataStore.getData(uuid);

                if (optionalProfile.isPresent()) {
                    final NickoProfile profile = optionalProfile.get();
                    final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Admin.CHECK,
                            player.getName(),
                            (profile.hasData() ? "§a✔" : "§c❌"),
                            (profile.getName() == null ? "§cN/A" : profile.getName()),
                            (profile.getSkin() == null ? "§cN/A" : profile.getSkin()));
                    skull.setDisplayName("§6" + translation.getName());
                    translation.getLore().forEach(skull::addLoreLines);
                } else {
                    // Default item name in case the profile is not found
                    skull.setDisplayName("§6§lYou should not see this!");
                    skull.addLoreLines(
                            "§cPlease file a bug report",
                            "§cat https://ineanto.xyz/git/ineanto/nicko!"
                    );
                }

                return skull;
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                NickoBukkit.getInstance().getLogger().severe("Unable to get head for specified UUID ( " + uuid + ")! (GUI/PlayerCheck)");
            }

            final ItemBuilder builder = new ItemBuilder(Material.TNT);
            final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.ERROR);
            builder.setDisplayName(translation.getName());
            translation.getLore().forEach(builder::addLoreLines);
            return builder;
        });
    }
}
