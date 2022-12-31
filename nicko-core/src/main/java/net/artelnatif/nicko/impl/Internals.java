package net.artelnatif.nicko.impl;

import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.disguise.ActionResult;
import org.bukkit.entity.Player;

public interface Internals {
    void updateSelf(Player player);

    void updateOthers(Player player);

    ActionResult updateProfile(Player player, NickoProfile profile, boolean skinChange);
}
