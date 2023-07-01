package xyz.atnrch.nicko.gui.items.admin.cache;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.gui.ConfirmGUI;
import xyz.atnrch.nicko.gui.admin.cache.CacheDetailedGUI;
import xyz.atnrch.nicko.gui.items.confirm.ActionCallback;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import java.util.UUID;

public class CacheEntry extends AsyncItem {
    private final String name;

    public CacheEntry(String name) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            final String stringUUID = name.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
            final UUID uuid = UUID.fromString(stringUUID);
            final SkullBuilder skull = new SkullBuilder(uuid);
            skull.setDisplayName("§6Skin Entry");
            skull.addLoreLines("§7Click to invalidate skin");
            return skull;
        });
        this.name = name;
    }

    @Override
    public void handleClick(@NotNull ClickType click, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (click.isLeftClick() || click.isRightClick()) {
            event.getView().close();
            new ConfirmGUI(player, new ActionCallback() {
                @Override
                public void onConfirm() {
                    player.sendMessage(name + " cleared");
                }

                @Override
                public void onCancel() {
                    new CacheDetailedGUI(player).open();
                }
            }).open();
        }
    }
}
