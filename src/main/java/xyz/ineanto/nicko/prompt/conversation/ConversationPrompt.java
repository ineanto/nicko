package xyz.ineanto.nicko.prompt.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.prompt.Prompt;
import xyz.ineanto.nicko.language.LanguageKey;

import java.util.Map;
import java.util.Objects;

public class ConversationPrompt extends Prompt {
    private final String changeBothTag = "changeBoth";
    private final Player player;
    private final ConversationFactory conversationFactory = new ConversationFactory(Nicko.getInstance())
            .withTimeout(30)
            .withModality(false)
            .withEscapeSequence("EXIT")
            .withLocalEcho(false)
            .thatExcludesNonPlayersWithMessage("Player only");

    private String name;

    public ConversationPrompt(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void displayNameThenSkinPrompt() {
        conversationFactory
                .withFirstPrompt(new ChangeNameConversation())
                .withInitialSessionData(Map.of(changeBothTag, true))
                .buildConversation(player)
                .begin();
    }

    @Override
    public void displaySkinPrompt() {
        conversationFactory
                .withFirstPrompt(new ChangeSkinConversation())
                .buildConversation(player)
                .begin();
    }

    @Override
    public void displayNamePrompt() {
        conversationFactory
                .withFirstPrompt(new ChangeNameConversation())
                .buildConversation(player)
                .begin();
    }

    private class ChangeNameConversation extends StringPrompt {
        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return playerLanguage.translate(LanguageKey.Event.Appearance.Set.CHAT_PROMPT_NAME, true);
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
            return playerLanguage.translate(LanguageKey.Event.Appearance.Set.CHAT_PROMPT_SKIN, true);
        }

        @Override
        public @Nullable org.bukkit.conversations.Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            update(name != null ? name : null, input, true);
            name = null;
            return END_OF_CONVERSATION;
        }
    }
}
