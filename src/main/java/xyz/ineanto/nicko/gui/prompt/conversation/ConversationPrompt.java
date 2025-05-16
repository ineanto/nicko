package xyz.ineanto.nicko.gui.prompt.conversation;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.gui.prompt.Prompt;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.Map;
import java.util.Objects;

public class ConversationPrompt extends Prompt {
    private final ConversationFactory conversationFactory = new ConversationFactory(Nicko.getInstance());
    private final String identifier;
    private final Player player;

    private String name;
    private String skin;

    public ConversationPrompt(Player player) {
        super(player);
        this.player = player;
        this.identifier = "nicko-conversation-" + player.getUniqueId();
    }


    @Override
    public void displayNameThenSkinPrompt() {
    }

    @Override
    public void displaySkinPrompt() {
        conversationFactory
                .thatExcludesNonPlayersWithMessage("Player only")
                .withTimeout(30)
                .withFirstPrompt(new NickoConversation(player, playerLanguage))
                .withInitialSessionData(Map.of(identifier, true, identifier + "-skin", true))
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    @Override
    public void displayNamePrompt() {
    }

    private class NickoConversation extends StringPrompt {
        private final Player player;
        private final PlayerLanguage playerLanguage;

        public NickoConversation(Player player, PlayerLanguage playerLanguage) {
            this.player = player;
            this.playerLanguage = playerLanguage;

        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return "Enter your skin";
        }

        @Override
        public @Nullable org.bukkit.conversations.Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
            if (Objects.equals(context.getSessionData(identifier + "-skin"), true)) {
                skin = input;
                update(null, skin, true);
                return END_OF_CONVERSATION;
            }

            return END_OF_CONVERSATION;
        }
    }
}
