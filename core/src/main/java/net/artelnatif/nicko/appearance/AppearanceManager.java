package net.artelnatif.nicko.appearance;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.google.common.collect.Multimap;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import net.artelnatif.nicko.storage.PlayerDataStore;
import net.artelnatif.nicko.storage.name.PlayerNameStore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AppearanceManager {
    private final NickoProfile profile;
    private final Player player;
    private final NickoBukkit instance = NickoBukkit.getInstance();
    private final PlayerDataStore dataStore = instance.getDataStore();
    private final PlayerNameStore nameStore = instance.getNameStore();

    private AppearanceManager(UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.profile = dataStore.getData(uuid).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    private AppearanceManager(String name) {
        this.player = null;
        this.profile = dataStore.getOfflineData(name).orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public static AppearanceManager get(Player player) {
        return new AppearanceManager(player.getUniqueId());
    }

    public static AppearanceManager get(String name) {
        return new AppearanceManager(name);
    }

    public boolean hasData() {
        return !profile.isEmpty();
    }

    public void setSkin(String skin) {
        profile.setSkin(skin);
    }

    public String getSkin() {
        return profile.getSkin();
    }

    public boolean needsASkinChange() {
        return profile.getSkin() != null && !profile.getSkin().equals(player.getName());
    }

    public void setName(String name) {
        profile.setName(name);
    }

    public String getName() {
        return profile.getName();
    }

    public NickoProfile getProfile() {
        return profile;
    }

    public void setNameAndSkin(String name, String skin) {
        this.profile.setName(name);
        this.profile.setSkin(skin);
        updatePlayer(true);
    }

    public ActionResult<Void> reset() {
        final String defaultName = nameStore.getStoredName(player);
        this.profile.setName(defaultName);
        this.profile.setSkin(defaultName);
        final ActionResult<Void> actionResult = resetPlayer();
        this.profile.setSkin(null);
        this.profile.setName(null);
        return actionResult;
    }

    public ActionResult<Void> resetPlayer() {
        // TODO: 4/3/23 Reset player
        return new ActionResult<>();
    }

    public ActionResult<Void> updatePlayer(boolean skinChange) {
        final String displayName = profile.getName() == null ? player.getName() : profile.getName();
        final WrappedGameProfile gameProfile = new WrappedGameProfile(player.getUniqueId(), displayName);

        final ActionResult<Void> result = updateGameProfileSkin(gameProfile, skinChange);
        if (!result.isError()) {
            updateTabList(gameProfile, displayName);
        }
        return new ActionResult<>();
    }

    private ActionResult<Void> updateGameProfileSkin(WrappedGameProfile gameProfile, boolean skinChange) {
        final boolean changeOnlyName = profile.getSkin() != null && !profile.getSkin().equalsIgnoreCase(player.getName());

        if (skinChange || changeOnlyName) {
            Optional<MojangSkin> skin;
            try {
                final MojangAPI mojang = NickoBukkit.getInstance().getMojangAPI();
                final Optional<String> uuid = mojang.getUUID(profile.getSkin());
                if (uuid.isPresent()) {
                    skin = mojang.getSkin(uuid.get());
                    if (skin.isPresent()) {
                        final MojangSkin skinResult = skin.get();
                        final Multimap<String, WrappedSignedProperty> properties = gameProfile.getProperties();
                        properties.removeAll("textures");
                        properties.put("textures", new WrappedSignedProperty("textures", skinResult.getValue(), skinResult.getSignature()));
                        Bukkit.broadcastMessage("Modified properties");
                    }
                }
                Bukkit.broadcastMessage("Respawning player");
                respawnPlayer();
                return new ActionResult<>();
            } catch (ExecutionException e) {
                return new ActionResult<>(I18NDict.Error.SKIN_FAIL_CACHE);
            } catch (IOException e) {
                return new ActionResult<>(I18NDict.Error.NAME_FAIL_MOJANG);
            }
        }
        return new ActionResult<>();
    }

    private void respawnPlayer() {
        final PacketContainer respawnOtherWorld = getRespawnPacket(Bukkit.getWorld("world_the_end"));
        final PacketContainer respawn = getRespawnPacket(player.getWorld());
        instance.getProtocolManager().sendServerPacket(player, respawnOtherWorld);
        instance.getProtocolManager().sendServerPacket(player, respawn);
    }

    private PacketContainer getRespawnPacket(World world) {
        final PacketContainer packet = new PacketContainer(PacketType.Play.Server.RESPAWN);
        final EnumWrappers.NativeGameMode gamemode = EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode());
        packet.getWorldKeys().write(0, world);
        packet.getLongs().write(0, world.getSeed());

        packet.getGameModes().write(0, gamemode); // gamemode
        packet.getGameModes().write(1, gamemode); // previous gamemode

        packet.getBooleans().write(0, false);
        packet.getBooleans().write(1, false);
        return packet;
    }

    private void updateTabList(WrappedGameProfile gameProfile, String displayName) {
        final PacketContainer infoAdd = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        infoAdd.getPlayerInfoActions().write(0, Set.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY
        ));

        infoAdd.getPlayerInfoDataLists().write(1, List.of(new PlayerInfoData(
                gameProfile,
                0,
                EnumWrappers.NativeGameMode.fromBukkit(player.getGameMode()),
                WrappedChatComponent.fromText(displayName)
        )));

        final PacketContainer infoRemove = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        infoRemove.getUUIDLists().write(0, List.of(player.getUniqueId()));

        instance.getProtocolManager().broadcastServerPacket(infoRemove);
        instance.getProtocolManager().broadcastServerPacket(infoAdd);
    }
}
