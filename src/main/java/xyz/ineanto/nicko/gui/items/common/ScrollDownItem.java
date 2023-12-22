package xyz.ineanto.nicko.gui.items.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.i18n.Translation;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem;

public class ScrollDownItem extends ScrollItem {
    final I18N i18n;

    public ScrollDownItem(I18N i18n) {
        super(1);
        this.i18n = i18n;
    }

    @Override
    public ItemProvider getItemProvider(ScrollGui gui) {
        final ItemBuilder builder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE);
        final Translation translation = i18n.translate(I18NDict.GUI.SCROLL_DOWN);
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

