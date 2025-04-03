package xyz.ineanto.nicko.gui.prompt;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.appearance.AppearanceManager;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;

import java.util.Optional;

public class PromptManager {
    private final AppearanceManager appearanceManager;
    private final NickoProfile profile;
    private final Player player;
    private final Prompt prompt;
    private final PlayerDataStore dataStore = Nicko.getInstance().getDataStore();
    private final PlayerLanguage playerLanguage;

    public PromptManager(Player player) {
        this.player = player;
        this.playerLanguage = new PlayerLanguage(player);

        final Optional<NickoProfile> optionalProfile = dataStore.getData(player.getUniqueId());
        this.profile = optionalProfile.orElse(NickoProfile.EMPTY_PROFILE.clone());
        this.appearanceManager = new AppearanceManager(player);
        this.prompt = new SignPrompt(player, playerLanguage);
    }

    public void displayNameThenSkinPrompt() {
        final Optional<String[]> optionalValues = prompt.displayNameThenSkinPrompt();

        if (optionalValues.isPresent()) {
            final String[] values = optionalValues.get();
            profile.setName(values[0]);
            profile.setSkin(values[1]);
            dataStore.updateCache(player.getUniqueId(), profile);
            update(true);
        }
    }

    public void displaySkinPromptThenUpdate() {
        final Optional<String> name = prompt.displaySkinPrompt();

        if (name.isPresent()) {
            profile.setName(name.get());
            dataStore.updateCache(player.getUniqueId(), profile);
            update(false);
        }
    }

    public void displayNamePromptThenUpdate() {
        final Optional<String> name = prompt.displayNamePrompt();

        if (name.isPresent()) {
            profile.setName(name.get());
            dataStore.updateCache(player.getUniqueId(), profile);
            update(false);
        }
    }

    private void update(boolean skinChange) {
        final ActionResult actionResult = appearanceManager.update(skinChange);
        if (!actionResult.isError()) {
            player.sendMessage(playerLanguage.translateWithWhoosh(LanguageKey.Event.Appearance.Set.OK));
            player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1f);
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