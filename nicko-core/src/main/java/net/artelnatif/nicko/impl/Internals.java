package net.artelnatif.nicko.impl;

import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;

public interface Internals {
    void updateSelf(Player player);

    void updateOthers(Player player);

    void updateProfile(Player player, NickoProfile profile, boolean skinChange);
}
