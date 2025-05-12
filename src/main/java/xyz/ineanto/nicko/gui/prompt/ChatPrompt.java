package xyz.ineanto.nicko.gui.prompt;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.event.custom.PromptCloseEvent;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatPrompt implements Prompt {
    private final Player player;
    private final PlayerLanguage playerLanguage;

    private String name = null;
    private String skin = null;
    private final List<UUID> inPrompt = new ArrayList<>();

    public ChatPrompt(Player player, PlayerLanguage playerLanguage) {
        this.player = player;
        this.playerLanguage = playerLanguage;
    }

    @Override
    public Optional<String[]> displayNameThenSkinPrompt() {
        promptInChat(false, false);

        if (skin == null || name == null) {
            return Optional.empty();
        }

        return Optional.of(new String[]{name, skin});
    }

    @Override
    public Optional<String> displaySkinPrompt() {
        promptInChat(true, true);
        return Optional.empty();
    }

    @Override
    public Optional<String> displayNamePrompt() {
        promptInChat(false, true);
        return Optional.empty();
    }

    private void promptInChat(boolean isSkin, boolean last) {
        // what the HELL did I wrote (Ineanto)
        // TODO (Ineanto, 11/05/2025): Doesn't work, the event is registered multiple times and thus is breaking the logic.
        if (!inPrompt.contains(player.getUniqueId())) {
            Bukkit.broadcast(Component.text("WAS NOT IN"));
            inPrompt.add(player.getUniqueId());
            Bukkit.getScheduler().runTaskLaterAsynchronously(Nicko.getInstance(), () -> inPrompt.remove(player.getUniqueId()), 20 * 15L);
        }

        if (isSkin) {
            player.sendMessage(playerLanguage.translate(LanguageKey.Event.Appearance.Set.CHAT_PROMPT, true, "skin"));
        } else {
            player.sendMessage(playerLanguage.translate(LanguageKey.Event.Appearance.Set.CHAT_PROMPT, true, "name"));
        }

        Nicko.getInstance().getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(AsyncChatEvent event) {
                if (!inPrompt.contains(event.getPlayer().getUniqueId())) {
                    Bukkit.broadcast(Component.text("IS NOT IN (chat)"));
                    return;
                }

                event.setCancelled(true);
                final String plain = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());

                if (isSkin) {
                    skin = plain;
                } else {
                    name = plain;
                }

                if (last) {
                    Bukkit.broadcast(Component.text("WAS LAST"));
                    inPrompt.remove(event.getPlayer().getUniqueId());
                    Bukkit.getScheduler().runTask(Nicko.getInstance(), () -> Bukkit
                            .getPluginManager()
                            .callEvent(new PromptCloseEvent(event.getPlayer(), skin, name))
                    );
                } else {
                    Bukkit.broadcast(Component.text("PROMPTING FOR SKIN NOW"));
                    promptInChat(true, true);
                }
            }
        }, Nicko.getInstance());
    }
}
