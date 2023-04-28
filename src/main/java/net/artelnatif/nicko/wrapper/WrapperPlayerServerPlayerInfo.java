package net.artelnatif.nicko.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
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

    public WrapperPlayerServerPlayerInfo(PacketContainer packet) {
        super(packet, TYPE);
    }

    public Set<EnumWrappers.PlayerInfoAction> getActions() {
        return handle.getPlayerInfoActions().read(0);
    }

    public void setActions(Set<EnumWrappers.PlayerInfoAction> value) {
        handle.getPlayerInfoActions().write(0, value);
    }

    public List<PlayerInfoData> getData() {
        return handle.getPlayerInfoDataLists().read(0);
    }

    public void setData(List<PlayerInfoData> value) {
        //handle.getSets(PlayerInfoData.getConverter()).write(0, value);
        handle.getPlayerInfoDataLists().write(1, value);
    }
}