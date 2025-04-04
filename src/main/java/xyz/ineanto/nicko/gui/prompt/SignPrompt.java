package xyz.ineanto.nicko.gui.prompt;

import de.rapha149.signgui.SignGUI;
import de.rapha149.signgui.SignGUIAction;
import de.rapha149.signgui.exception.SignGUIVersionException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SignPrompt implements Prompt {
    private final CompletableFuture<Void> future = new CompletableFuture<>();

    private final Player player;
    private final PlayerLanguage playerLanguage;
    private final ArrayList<String> lines = new ArrayList<>(
            List.of(
                    "VVVVVVVVVVVVVVV",
                    "",
                    "",
                    "ΛΛΛΛΛΛΛΛΛΛΛΛΛΛΛ"
            )
    );

    private String name = null;
    private String skin = null;

    public SignPrompt(Player player, PlayerLanguage playerLanguage) {
        this.player = player;
        this.playerLanguage = playerLanguage;
    }

    @Override
    public Optional<String[]> displayNameThenSkinPrompt() {
        displayNamePrompt();
        displaySkinPrompt();

        if (skin == null || name == null) {
            return Optional.empty();
        }

        return Optional.of(new String[]{name, skin});
    }

    @Override
    public Optional<String> displaySkinPrompt() {
        this.lines.set(1, playerLanguage.translate(LanguageKey.GUI.NEW_SKIN, false));
        displaySign(true);

        synchronized (future) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        if (skin == null) {
            return Optional.empty();
        }

        return Optional.of(skin);
    }

    @Override
    public Optional<String> displayNamePrompt() {
        this.lines.set(1, playerLanguage.translate(LanguageKey.GUI.NEW_NAME, false));
        displaySign(false);

        synchronized (future) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        if (name == null) {
            return Optional.empty();
        }

        return Optional.of(skin);
    }

    private void displaySign(boolean isSkin) {
        try {
            final SignGUI gui = SignGUI.builder()
                    .setLines(lines.toArray(new String[0]))
                    .setType(Material.OAK_SIGN)
                    .callHandlerSynchronously(Nicko.getInstance())
                    .setHandler((_, result) -> {
                        final String internalLine1 = result.getLineWithoutColor(1);

                        if (internalLine1.isEmpty()) {
                            return List.of(SignGUIAction.displayNewLines(lines.toArray(new String[0])));
                        }

                        if (isSkin) {
                            skin = internalLine1;
                        } else {
                            name = internalLine1;
                        }

                        future.complete(null);
                        return Collections.emptyList();
                    })
                    .build();

            gui.open(player);
        } catch (SignGUIVersionException _) { }
    }
}
