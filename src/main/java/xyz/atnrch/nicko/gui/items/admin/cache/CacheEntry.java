package xyz.atnrch.nicko.gui.items.admin.cache;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.gui.ConfirmGUI;
import xyz.atnrch.nicko.gui.admin.cache.CacheDetailedGUI;
import xyz.atnrch.nicko.gui.items.confirm.ActionCallback;
import xyz.atnrch.nicko.mojang.MojangAPI;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.builder.SkullBuilder;
import xyz.xenondevs.invui.item.impl.AsyncItem;

import java.util.UUID;

public class CacheEntry extends AsyncItem {
    private final String name;
    private final String uuid;
    private final MojangAPI mojangAPI = NickoBukkit.getInstance().getMojangAPI();

    public CacheEntry(String uuid) {
        super(new ItemBuilder(Material.PAINTING).setDisplayName("§7§oLoading..."), () -> {
            final String dashedUuid = uuid.replaceAll("(.{8})(.{4})(.{4})(.{4})(.+)", "$1-$2-$3-$4-$5");
            final UUID uuidObject = UUID.fromString(dashedUuid);
            final SkullBuilder skull = new SkullBuilder(uuidObject);
            skull.setDisplayName("§6" + NickoBukkit.getInstance().getMojangAPI().getUUIDName(uuid));
            skull.addLoreLines("§7Click to invalidate skin");
            return skull;
        });
        this.uuid = uuid;
        this.name = mojangAPI.getUUIDName(uuid);
    }

    @Override
    public void handleClick(@NotNull ClickType click, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (click.isLeftClick() || click.isRightClick()) {
            event.getView().close();
            new ConfirmGUI(player, new ActionCallback() {
                @Override
                public void onConfirm() {
                    mojangAPI.eraseFromCache(uuid);
                    player.sendMessage(name + " has been erased from the cache.");
                }

                @Override
                public void onCancel() {
                    new CacheDetailedGUI(player).open();
                }
            }).open();
        }
    }
}
