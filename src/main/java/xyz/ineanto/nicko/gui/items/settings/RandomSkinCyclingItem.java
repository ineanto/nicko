package xyz.ineanto.nicko.gui.items.settings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.i18n.Translation;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.PlayerDataStore;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.CycleItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.Optional;

public class RandomSkinCyclingItem {
    private final Player player;
    private final ItemProvider[] providers;
    private final I18N i18n;

    public RandomSkinCyclingItem(Player player) {
        this.player = player;
        this.i18n = new I18N(player);
        this.providers = new ItemProvider[]{
                getItemProviderForValue(true),
                getItemProviderForValue(false)
        };
    }

    public AbstractItem get() {
        final PlayerDataStore dataStore = NickoBukkit.getInstance().getDataStore();
        final Optional<NickoProfile> profile = dataStore.getData(player.getUniqueId());
        if (profile.isPresent()) {
            final NickoProfile nickoProfile = profile.get();
            int localeOrdinal = nickoProfile.isRandomSkin() ? 0 : 1;
            return CycleItem.withStateChangeHandler((observer, integer) -> {
                observer.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 0.707107f); // 0.707107 ~= C
                nickoProfile.setRandomSkin(integer != 1);
                if (dataStore.updateCache(player.getUniqueId(), nickoProfile).isError()) {
                    player.sendMessage(i18n.translate(I18NDict.Event.Settings.ERROR, true));
                    player.getOpenInventory().close();
                }
            }, localeOrdinal, providers);
        }

        return new SimpleItem(ItemProvider.EMPTY);
    }

    private ItemProvider getItemProviderForValue(boolean enabled) {
        final SkullBuilder.HeadTexture texture = new SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzgzMTEzOGMyMDYxMWQzMDJjNDIzZmEzMjM3MWE3NDNkMTc0MzdhMTg5NzNjMzUxOTczNDQ3MGE3YWJiNCJ9fX0=");
        final SkullBuilder builder = new SkullBuilder(texture);
        final Translation randomSkinTranslation = i18n.translateAndReplace(I18NDict.GUI.Settings.RANDOM_SKIN);
        final Translation toggleableTranslation = i18n.translateAndReplace(I18NDict.GUI.Settings.TOGGLEABLE_BUTTON,
                (enabled ? "§7>§c" : "§6§l>§c§l"),
                (enabled ? "§6§l>§a§l" : "§7>§a")
        );
        final Translation cyclingChoicesTranslation = i18n.translateAndReplace(I18NDict.GUI.Settings.CYCLING_CHOICES);

        builder.setDisplayName(randomSkinTranslation.name());
        toggleableTranslation.lore().forEach(builder::addLoreLines);

        cyclingChoicesTranslation.lore().replaceAll(s -> {
            final Component deserializedLoreLine = MiniMessage.miniMessage().deserialize(s);
            return LegacyComponentSerializer.legacySection().serialize(deserializedLoreLine);
        });
        cyclingChoicesTranslation.lore().forEach(builder::addLoreLines);
        return builder;
    }
}