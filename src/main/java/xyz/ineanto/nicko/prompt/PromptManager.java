package xyz.ineanto.nicko.prompt;

import org.bukkit.entity.Player;
import xyz.ineanto.nicko.prompt.conversation.ConversationPrompt;

public class PromptManager {
    private final Prompt prompt;

    public PromptManager(Player player) {
        this.prompt = new ConversationPrompt(player);
    }

    public void displayNameThenSkinPrompt() {
        prompt.displayNameThenSkinPrompt();
    }

    public void displaySkinPrompt() {
        prompt.displaySkinPrompt();
    }

    public void displayNamePromptThenUpdate() {
        prompt.displayNamePrompt();
    }
}