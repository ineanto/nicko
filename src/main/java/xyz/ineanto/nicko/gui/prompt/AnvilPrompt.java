package xyz.ineanto.nicko.gui.prompt;

import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.mojang.MojangUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AnvilPrompt implements Prompt {
    private final Player player;
    private final PlayerLanguage playerLanguage;

    private final AtomicReference<Optional<String>> name = new AtomicReference<>();
    private final AtomicReference<Optional<String>> skin = new AtomicReference<>();

    public AnvilPrompt(Player player, PlayerLanguage playerLanguage) {
        this.player = player;
        this.playerLanguage = playerLanguage;
    }

    @Override
    public Optional<String[]> displayNameThenSkinPrompt() {
        new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            // Praying that it works. This is untested code!
                            name.set(Optional.of(snapshot.getText()));

                            final Optional<String> skinFromAnvil = displaySkinPrompt();
                            skin.set(skinFromAnvil);
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...").open(player);

        if (name.get().isEmpty() || skin.get().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new String[]{name.get().orElse(player.getName()), skin.get().orElse(player.getName())});
    }

    @Override
    public Optional<String> displaySkinPrompt() {
        new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(true))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            skin.set(Optional.of(snapshot.getText()));
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New skin...").open(player);
        return skin.get();
    }

    @Override
    public Optional<String> displayNamePrompt() {
        new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            name.set(Optional.of(snapshot.getText()));
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...").open(player);
        return name.get();
    }

    private ItemStack getLeftItem(boolean skin) {
        final ItemStack item = new ItemStack(Material.PAPER);
        final ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(Component.text(playerLanguage.translate(skin ? LanguageKey.GUI.NEW_SKIN : LanguageKey.GUI.NEW_NAME, false)));
        }

        item.setItemMeta(meta);
        return item;
    }
}
