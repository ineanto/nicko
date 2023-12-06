package xyz.ineanto.nicko.gui.items;

import org.bukkit.Material;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.xenondevs.invui.item.builder.AbstractItemBuilder;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;

public class ItemDefaults {
    public static AbstractItemBuilder<?> getErrorSkullItem(I18N i18n, String key, Object... args) {
        // "Missing Value" (Valve's signature missing texture) Texture Value
        final SkullBuilder.HeadTexture headTexture = new SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNmZTU5YjJhMWQyYmYzMjcwNDA2OGVmYzg2MGM3NWY5MjEyYzIzMTBiNDNkMDdjNGJiYTRiNGViMjM0ZTY4NCJ9fX0=");
        final SkullBuilder builder = new SkullBuilder(headTexture);
        return i18n.translateItem(builder, key, args);
    }

    public static AbstractItemBuilder<?> getUnavailableItem(I18N i18n) {
        final ItemBuilder builder = new ItemBuilder(Material.RED_TERRACOTTA);
        return i18n.translateItem(builder, I18NDict.GUI.UNAVAILABLE);
    }
}
