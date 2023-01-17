package net.artelnatif.nicko.gui.items.admin.cache;

import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.AsyncItem;
import org.bukkit.Material;

import java.util.UUID;

public class SkinPlaceholder extends AsyncItem {
    public SkinPlaceholder(String name) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            String uuid = name.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
            final SkullBuilder skull = new SkullBuilder(UUID.fromString(uuid));
            skull.setDisplayName("Skin");
            skull.addLoreLines("§7Click to invalidate skin");
            return skull;
        });
    }
}
