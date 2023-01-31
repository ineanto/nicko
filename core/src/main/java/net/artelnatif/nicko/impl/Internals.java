package net.artelnatif.nicko.impl;

import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.bukkit.i18n.I18NDict;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface Internals {
    void updateSelf(Player player);

    void updateOthers(Player player);

    ActionResult<Void> updateProfile(Player player, NickoProfile profile, boolean skinChange, boolean reset);

    default ActionResult<MojangSkin> fetchSkinTextures(NickoProfile profile, boolean reset) {
        Optional<MojangSkin> skin;
        try {
            final MojangAPI mojang = NickoBukkit.getInstance().getNicko().getMojangAPI();
            final Optional<String> uuid = mojang.getUUID(profile.getSkin());
            if (uuid.isPresent()) {
                skin = (reset ? mojang.getSkinWithoutCaching(uuid.get()) : mojang.getSkin(uuid.get()));
                if (skin.isEmpty()) {
                    return new ActionResult<>(I18NDict.Error.SKIN_FAIL_MOJANG);
                }

                final ActionResult<MojangSkin> actionResult = new ActionResult<>();
                actionResult.setResult(skin.get());
                return actionResult;
            }
            return new ActionResult<>(I18NDict.Error.NAME_FAIL_MOJANG);
        } catch (ExecutionException e) {
            return new ActionResult<>(I18NDict.Error.SKIN_FAIL_CACHE);
        } catch (IOException e) {
            return new ActionResult<>(I18NDict.Error.NAME_FAIL_MOJANG);
        }
    }
}
