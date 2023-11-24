package xyz.atnrch.nicko.gui.items.admin.check;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.items.ItemDefaults;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
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
            return i18n.translateItem(builder, I18NDict.GUI.LOADING);
        }, (click -> true)).getItemProvider(), () -> {
            final Player player = Bukkit.getPlayer(uuid);
            try {
                final SkullBuilder skull = new SkullBuilder(uuid);
                final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
                final Optional<NickoProfile> optionalProfile = dataStore.getData(uuid);

                if (optionalProfile.isPresent()) {
                    final NickoProfile profile = optionalProfile.get();
                    return i18n.translateItem(skull, I18NDict.GUI.Admin.CHECK,
                            player.getName(),
                            (profile.hasData() ? "§a✔" : "§c❌"),
                            (profile.getName() == null ? "§7N/A" : profile.getName()),
                            (profile.getSkin() == null ? "§7N/A" : profile.getSkin()));
                }
            } catch (MojangApiUtils.MojangApiException | IOException e) {
                NickoBukkit.getInstance().getLogger().severe("Unable to get head for specified UUID ( " + uuid + ")! (GUI/PlayerCheck)");
            }

            return ItemDefaults.getErrorSkullItem(i18n, I18NDict.GUI.Admin.CHECK,
                    "§4???", "§cN/A", "§cN/A", "§cN/A"
            );
        });
    }
}
