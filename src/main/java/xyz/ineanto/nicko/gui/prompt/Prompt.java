package xyz.ineanto.nicko.gui.prompt;

import java.util.Optional;

public interface Prompt {
    Optional<String[]> displayNameThenSkinPrompt();

    Optional<String> displaySkinPrompt();

    Optional<String> displayNamePrompt();
}
