package net.artelnatif.nicko.impl;

import com.comphenix.protocol.ProtocolManager;
import net.artelnatif.nicko.NickoBukkit;

public interface InternalsProtocolLib extends Internals {
    default ProtocolManager getProtocolLib() {
        return NickoBukkit.getInstance().getProtocolManager();
    }
}
