package xyz.ineanto.nicko.gui.prompt;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SignPrompt implements Prompt {
    private final Player player;
    private final PlayerLanguage playerLanguage;

    private List<String> lines = List.of(
            "VVVVVVVVVVVVVVV",
            null,
            null,
            "ΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛ"
    );

    private final AtomicReference<Optional<String>> name = new AtomicReference<>();
    private final AtomicReference<Optional<String>> skin = new AtomicReference<>();

    public SignPrompt(Player player, PlayerLanguage playerLanguage) {
        this.player = player;
        this.playerLanguage = playerLanguage;
    }

    @Override
    public Optional<String[]> displayNameThenSkinPrompt() {
        try {
            final SignGUI gui = SignGUI.builder()
                    .setLines(lines.toArray(new String[0]))
                    .setType(Material.OAK_SIGN)
                    .setHandler((_, result) -> {
                        String internalLine2 = result.getLineWithoutColor(2);

                        if (internalLine2.isEmpty()) {
                            return List.of(SignGUIAction.displayNewLines(lines.toArray(new String[0])));
                        }

                        name.set(Optional.of(internalLine2));
                        return Collections.emptyList();
                    })
                    .build();

            gui.open(player);
            return Optional.of(new String[]{name.get().orElse(player.getName()), skin.get().orElse(player.getName())});
        } catch (SignGUIVersionException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> displaySkinPrompt() {
        this.lines.set(1, playerLanguage.translate(LanguageKey.GUI.NEW_SKIN, false));
        return displaySign(skin);
    }

    @Override
    public Optional<String> displayNamePrompt() {
        this.lines.set(1, playerLanguage.translate(LanguageKey.GUI.NEW_NAME, false));
        return displaySign(name);
    }

    private Optional<String> displaySign(AtomicReference<Optional<String>> reference) {
        try {
            final SignGUI gui = SignGUI.builder()
                    .setLines(lines.toArray(new String[0]))
                    .setType(Material.OAK_SIGN)
                    .setHandler((_, result) -> {
                        final String internalLine2 = result.getLineWithoutColor(2);

                        if (internalLine2.isEmpty()) {
                            return List.of(SignGUIAction.displayNewLines(lines.toArray(new String[0])));
                        }

                        reference.set(Optional.of(internalLine2));
                        return Collections.emptyList();
                    })
                    .build();

            gui.open(player);
            return reference.get();
        } catch (SignGUIVersionException exception) {
            return Optional.empty();
        }
    }
}
