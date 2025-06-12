package xyz.ineanto.nicko.appearance;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.event.custom.PlayerDisguiseEvent;
import xyz.ineanto.nicko.event.custom.PlayerResetDisguiseEvent;
import xyz.ineanto.nicko.packet.PacketSender;
import xyz.ineanto.nicko.packet.PaperPacketSender;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.ineanto.nicko.storage.name.PlayerNameStore;

import java.util.Optional;

public class AppearanceManager {
    private final Nicko instance = Nicko.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();
    private final PacketSender packetSender;

    private final Player player;

    public AppearanceManager(Player player) {
        this.player = player;
        this.packetSender = new PaperPacketSender(player, getNickoProfile());
    }

    public ActionResult reset() {
        final NickoProfile profile = getNickoProfile();

        // Call the event.
        final PlayerResetDisguiseEvent event = new PlayerResetDisguiseEvent(player);
        Bukkit.getPluginManager().callEvent(event);

        profile.setName(null);
        profile.setSkin(null);
        dataStore.getCache().cache(player.getUniqueId(), profile);

        return ActionResult.error();
    }

    public ActionResult update(boolean skinChange) {
        final NickoProfile profile = getNickoProfile();
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();

        final ActionResult result = packetSender.updatePlayerProfile(displayName);

        if (skinChange) {
            final ActionResult propertiesUpdateResult = packetSender.updatePlayerProfileProperties();

            if (propertiesUpdateResult.isError()) {
                return reset();
            }
        }

        // Call the event.
        final PlayerDisguiseEvent event = new PlayerDisguiseEvent(player, profile.getSkin(), profile.getName());
        Bukkit.getPluginManager().callEvent(event);

        packetSender.sendEntityMetadataUpdate();
        packetSender.sendTabListUpdate(displayName);
        return result;
    }

    private NickoProfile getNickoProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        return optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
    }
}