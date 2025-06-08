package xyz.ineanto.nicko.prompt;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class Prompt {
    private final Player player;
    private final AppearanceManager appearanceManager;
    private final NickoProfile profile;
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();

    protected final PlayerLanguage playerLanguage;

    public Prompt(Player player) {
        this.player = player;
        this.appearanceManager = new AppearanceManager(player);
        this.playerLanguage = new PlayerLanguage(player);

        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        this.profile = optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
    }

    public abstract void displayNameThenSkinPrompt();

    public abstract void displaySkinPrompt();

    public abstract void displayNamePrompt();

    public void update(@Nullable String name, @Nullable String skin, boolean skinChange) {
        if (name != null && !name.isBlank()) {
            profile.setName(name);
        }

        if (skin != null && !skin.isBlank()) {
            profile.setSkin(skin);
        }

        dataStore.updateCache(player.getUniqueId(), profile);

        final ActionResult actionResult = appearanceManager.update(skinChange);
        if (!actionResult.isError()) {
            player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Set.OK));
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
        } else {
            player.sendMessage(
                    playerLanguage.translateWithOops(
                            LanguageKey.Event.Appearance.Set.ERROR,
                            playerLanguage.translate(actionResult.getErrorKey(), false)
                    ));
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1f, 1f);
        }
    }
}
