package net.artelnatif.nicko.impl;

import io.vavr.control.Either;
import net.artelnatif.nicko.disguise.NickoProfile;
import org.bukkit.entity.Player;

public interface Internals {
    void updateSelf(Player player);
    void updateOthers(Player player);
    Either<String, Void> updateProfile(Player player, NickoProfile profile, boolean skinChange);
}
