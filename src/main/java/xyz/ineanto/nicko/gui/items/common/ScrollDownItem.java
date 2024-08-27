package xyz.ineanto.nicko.gui.items.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.Translation;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem;

public class ScrollDownItem extends ScrollItem {
    final PlayerLanguage playerLanguage;

    public ScrollDownItem(PlayerLanguage playerLanguage) {
        super(1);
        this.playerLanguage = playerLanguage;
    }

    @Override
    public ItemProvider getItemProvider(ScrollGui gui) {
        final ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        final Translation translation = playerLanguage.translateAndReplace(LanguageKey.GUI.SCROLL_DOWN);
        builder.setDisplayName(Component.text(translation.name()).content());
        if (!gui.canScroll(1)) {
            // Lore serialization
            translation.lore().replaceAll(s -> {
                final Component deserializedLoreLine = MiniMessage.miniMessage().deserialize(s);
                return LegacyComponentSerializer.legacySection().serialize(deserializedLoreLine);
            });
            translation.lore().forEach(builder::addLoreLines);
        }
        return builder;
    }
}

