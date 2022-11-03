package net.artelnatif.nicko.impl;

import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.disguise.UpdateResult;
import org.bukkit.entity.Player;

public interface Internals {
    void updateSelf(Player player);

    void updateOthers(Player player);

    UpdateResult updateProfile(Player player, NickoProfile profile, boolean skinChange);
}
