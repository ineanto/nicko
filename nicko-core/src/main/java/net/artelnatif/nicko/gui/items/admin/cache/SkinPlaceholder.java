package net.artelnatif.nicko.gui.items.admin.cache;

import de.studiocode.invui.item.builder.ItemBuilder;
import de.studiocode.invui.item.builder.SkullBuilder;
import de.studiocode.invui.item.impl.AsyncItem;
import org.bukkit.Material;

public class SkinPlaceholder extends AsyncItem {
    public SkinPlaceholder(String name) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            System.out.println("name = " + name);
            final SkullBuilder skull = new SkullBuilder(name);
            skull.setDisplayName(name);
            skull.addLoreLines("§7Click to invalidate skin");
            return skull;
        });
    }
}
