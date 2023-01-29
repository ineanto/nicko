package net.artelnatif.nicko.bukkit.gui.items.admin.cache;

import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.AsyncItem;
import org.bukkit.Material;

import java.util.UUID;

public class SkinPlaceholder extends AsyncItem {
    public SkinPlaceholder(String name) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            final String stringUUID = name.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
            final UUID uuid = UUID.fromString(stringUUID);
            final SkullBuilder skull = new SkullBuilder(uuid);
            skull.setDisplayName("§6Skin Entry");
            skull.addLoreLines("§7Click to invalidate skin");
            return skull;
        });
    }
}
