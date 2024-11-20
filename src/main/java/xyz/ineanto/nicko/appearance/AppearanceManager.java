package xyz.ineanto.nicko.appearance;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.packet.InternalPacketSender;
import xyz.ineanto.nicko.packet.PacketSender;
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
        this.packetSender = new InternalPacketSender(player, getNickoProfile());
    }

    public ActionResult reset(boolean apply) {
        final NickoProfile profile = getNickoProfile();
        final String defaultName = nameStore.getStoredName(player);

        profile.setName(defaultName);
        profile.setSkin(defaultName);
        dataStore.getCache().cache(player.getUniqueId(), profile);

        if (apply) {
            final ActionResult result = update(true, true);

            profile.setName(null);
            profile.setSkin(null);
            dataStore.getCache().cache(player.getUniqueId(), profile);

            return result;
        }

        return ActionResult.ok();
    }

    public ActionResult update(boolean skinChange, boolean reset) {
        final NickoProfile profile = getNickoProfile();
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();

        final ActionResult result = packetSender.sendGameProfileUpdate(displayName, skinChange, reset);

        if (result.isError()) {
            return reset(false);
        }

        packetSender.sendEntityMetadataUpdate();
        packetSender.sendTabListUpdate(displayName);
        respawnPlayer();
        packetSender.sendEntityRespawn();

        return result;
    }

    private void respawnPlayer() {
        final boolean wasFlying = player.isFlying();
        final boolean wasAllowedToFly = player.getAllowFlight();
        final int foodLevel = player.getFoodLevel();

        packetSender.sendPlayerRespawn();

        player.teleport(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        player.setAllowFlight(wasAllowedToFly);
        player.setFlying(wasFlying);
        player.updateInventory();
        player.sendHealthUpdate();
        player.setFoodLevel(foodLevel);
    }

    private NickoProfile getNickoProfile() {
        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        return optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
    }
}