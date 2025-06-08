package xyz.ineanto.nicko.prompt.anvil;

import net.kyori.adventure.text.Component;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.prompt.Prompt;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.mojang.MojangUtils;

import java.util.Collections;

/**
 * This is currently unused, I'm waiting on AnvilGUI
 * to be compiled against Paper mappings.
 */
// TODO (Ineanto, 16/05/2025): Do some validation on the inputs
public class AnvilPrompt extends Prompt {
    private final Player player;

    private String name;
    private String skin;

    public AnvilPrompt(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void displayNameThenSkinPrompt() {
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
                            name = snapshot.getText();
                            displaySkinPrompt();
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...").open(player);
    }

    @Override
    public void displaySkinPrompt() {
        new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(true))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            skin = snapshot.getText();
                            update(name == null ? null : name, skin, true);
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New skin...").open(player);
    }

    @Override
    public void displayNamePrompt() {
        new AnvilGUI.Builder()
                .plugin(Nicko.getInstance())
                .itemLeft(getLeftItem(false))
                .interactableSlots(AnvilGUI.Slot.OUTPUT)
                .onClick((slot, snapshot) -> {
                    if (slot == AnvilGUI.Slot.OUTPUT) {
                        if (MojangUtils.isUsernameInvalid(snapshot.getText())) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Invalid username!"));
                        } else {
                            update(snapshot.getText(), null, false);
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        }
                    }
                    return Collections.emptyList();
                })
                .text("New name...").open(player);
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
