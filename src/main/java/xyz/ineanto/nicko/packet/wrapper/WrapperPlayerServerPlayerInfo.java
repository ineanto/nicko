package xyz.ineanto.nicko.packet.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;

import java.util.List;
import java.util.Set;

/**
 * Up-to-date version of the Wrapper class
 * for the PlayerServerPlayerInfo.
 *
 * @author ineanto, based on work from dmulloy2 and Kristian S. Strangeland
 */
public class WrapperPlayerServerPlayerInfo extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.PLAYER_INFO;

    public WrapperPlayerServerPlayerInfo() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public void setActions(Set<EnumWrappers.PlayerInfoAction> value) {
        if (MinecraftVersion.FEATURE_PREVIEW_UPDATE.atOrAbove()) {
            handle.getPlayerInfoActions().writeSafely(0, value);
        } else {
            handle.getPlayerInfoAction().writeSafely(0, value.stream().iterator().next()); // Get the first Value.
        }
    }

    public void setData(List<PlayerInfoData> value) {
        handle.getPlayerInfoDataLists().writeSafely(1, value);
    }
}
