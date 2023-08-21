package xyz.atnrch.nicko.gui.items.admin.check;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import java.util.Optional;
import java.util.UUID;

public class PlayerInformationItem extends AsyncItem {
    public PlayerInformationItem(UUID uuid) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            final Player player = Bukkit.getPlayer(uuid);
            final SkullBuilder skull = new SkullBuilder(uuid);
            final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
            final Optional<NickoProfile> optionalProfile = dataStore.getData(uuid);

            optionalProfile.ifPresent(profile -> {
                if (profile.hasData()) {
                    skull.addLoreLines(
                            "§cNicked: §a✔",
                            "§cName: §6" + profile.getName(),
                            "§cSkin: §6" + profile.getSkin()
                    );
                } else {
                    skull.addLoreLines(
                            "§cNicked: §c❌",
                            "§cName: §7N/A",
                            "§cSkin: §7N/A"
                    );
                }

                skull.setDisplayName("§6" + player.getName());
            });

            return skull;
        });
    }
}
