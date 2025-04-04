package xyz.ineanto.nicko.gui.prompt;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.Optional;

public class ChatPrompt implements Prompt {
    private final Player player;
    private final PlayerLanguage playerLanguage;

    private String name = null;
    private String skin = null;

    public ChatPrompt(Player player, PlayerLanguage playerLanguage) {
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
        promptInChat(true);
        return Optional.empty();
    }

    @Override
    public Optional<String> displayNamePrompt() {
        promptInChat(false);
        return Optional.empty();
    }

    private void promptInChat(boolean isSkin) {
        player.sendMessage(playerLanguage.translate(isSkin ? LanguageKey.GUI.NEW_SKIN : LanguageKey.GUI.NEW_NAME, false));
        //player.sendMessage(playerLanguage.translate(LanguageKey.GUI.ENTER_IN_CHAT, false));

        Nicko.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncChatEvent event) {
                if (event.getPlayer().equals(player)) {
                    event.setCancelled(true);
                    final String plain = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());

                    if (isSkin) {
                        skin = plain;
                    } else {
                        name = plain;
                    }
                }
            }
        }, Nicko.getInstance());
    }
}
