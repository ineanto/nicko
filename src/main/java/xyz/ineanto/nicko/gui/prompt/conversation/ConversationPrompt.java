package xyz.ineanto.nicko.gui.prompt.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.gui.prompt.Prompt;

import java.util.Map;
import java.util.Objects;

public class ConversationPrompt extends Prompt {
    private final ConversationFactory conversationFactory = new ConversationFactory(Nicko.getInstance());
    private final String changeBothTag = "changeBoth";
    private final Player player;

    private String name;

    public ConversationPrompt(Player player) {
        super(player);
        this.player = player;
    }


    @Override
    public void displayNameThenSkinPrompt() {
        conversationFactory
                .thatExcludesNonPlayersWithMessage("Player only")
                .withTimeout(30)
                .withModality(false)
                .withFirstPrompt(new ChangeNameConversation())
                .withEscapeSequence("EXIT")
                .withInitialSessionData(Map.of(changeBothTag, true))
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    @Override
    public void displaySkinPrompt() {
        conversationFactory
                .thatExcludesNonPlayersWithMessage("Player only")
                .withModality(false)
                .withTimeout(30)
                .withFirstPrompt(new ChangeSkinConversation())
                .withEscapeSequence("EXIT")
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    @Override
    public void displayNamePrompt() {
        conversationFactory
                .thatExcludesNonPlayersWithMessage("Player only")
                .withModality(false)
                .withTimeout(30)
                .withFirstPrompt(new ChangeNameConversation())
                .withEscapeSequence("EXIT")
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    private class ChangeNameConversation extends StringPrompt {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Enter your new name";
        }

        @Override
        public @Nullable org.bukkit.conversations.Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (Objects.equals(context.getSessionData(changeBothTag), true)) {
                name = input;
                return new ChangeSkinConversation();
            }

            update(input, null, false);
            return END_OF_CONVERSATION;
        }
    }

    private class ChangeSkinConversation extends StringPrompt {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Enter your new skin";
        }

        @Override
        public @Nullable org.bukkit.conversations.Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            update(name != null ? name : null, input, true);
            name = null;
            return END_OF_CONVERSATION;
        }
    }
}
