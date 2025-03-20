package net.wesjd.anvilgui.version;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public final class PaperWrapper1_21_R4 implements VersionWrapper {
    private int getRealNextContainerId(Player player) {
        return toNMS(player).nextContainerCounter();
    }


    private ServerPlayer toNMS(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public int getNextContainerId(Player player, AnvilContainerWrapper container) {
        return ((AnvilContainer) container).containerId;
    }

    @Override
    public void handleInventoryCloseEvent(Player player) {
        //CraftEventFactory.handleInventoryCloseEvent(toNMS(player));
        toNMS(player).doCloseContainer(); // q -> doCloseContainer
    }

    @Override
    public void sendPacketOpenWindow(Player player, int containerId, Object inventoryTitle) {
        toNMS(player).connection.send(new ClientboundOpenScreenPacket(containerId, MenuType.ANVIL, Component.literal(inventoryTitle.toString())));
    }

    @Override
    public void sendPacketCloseWindow(Player player, int containerId) {
        toNMS(player).connection.send(new ClientboundContainerClosePacket(containerId));
    }

    @Override
    public void sendPacketExperienceChange(Player player, int experienceLevel) {
        toNMS(player).connection.send(new ClientboundSetExperiencePacket(0f, 0, experienceLevel));
    }

    @Override
    public void setActiveContainerDefault(Player player) {
        toNMS(player).containerMenu = toNMS(player).inventoryMenu; // cd -> containerMenu, cc -> inventoryMenu
    }

    @Override
    public void setActiveContainer(Player player, AnvilContainerWrapper container) {
        toNMS(player).containerMenu = (AbstractContainerMenu) container;
    }

    @Override
    public void setActiveContainerId(AnvilContainerWrapper container, int containerId) { }

    @Override
    public void addActiveContainerSlotListener(AnvilContainerWrapper container, Player player) {
        toNMS(player).initMenu((AbstractContainerMenu) container);
    }

    @Override
    public AnvilContainerWrapper newContainerAnvil(Player player, Object title) {
        return new AnvilContainer(player, getRealNextContainerId(player), Component.literal(title.toString()));
    }

    @Override
    public Object literalChatComponent(String content) {
        return Component.literal(content); // IChatBaseComponent.b -> Component.literal
    }

    @Override
    public Object jsonChatComponent(String json) {
        return Component.Serializer.toJson(Component.literal(json), RegistryAccess.EMPTY);
    }

    private static class AnvilContainer extends AnvilMenu implements AnvilContainerWrapper {
        public AnvilContainer(Player player, int containerId, Component guiTitle) {
            super(
                    containerId,
                    ((CraftPlayer) player).getHandle().getInventory(),
                    ContainerLevelAccess.create(((CraftPlayer) player).getHandle().level(), BlockPos.ZERO)
            );
            this.checkReachable = false;
            setTitle(guiTitle);
        }

        @Override
        public void createResult() {
            // If the output is empty, copy the left input into the output
            Slot output = getSlot(2); // b -> getSlot
            if (!output.hasItem()) { // h -> hasItem
                output.set(getSlot(0).getItem().copy()); // f -> set, g -> getItem, v -> copy
            }

            this.cost.set(0); // y -> cost, a -> set

            // Sync to the client
            this.sendAllDataToRemote(); // b -> sendAllDataToRemote
            this.broadcastChanges(); // d -> broadcastChanges
        }

        @Override
        public boolean setItemName(@NotNull String itemName) {
            Slot inputLeft = getSlot(0);
            if (inputLeft.hasItem()) {
                inputLeft
                        .getItem()
                        .applyComponents(DataComponentPatch
                                .builder()
                                .set(DataComponents.CUSTOM_NAME, Component.literal(itemName))
                                .build()
                        );
                return true;
            }

            return false;
        }

        @Override
        public Inventory getBukkitInventory() {
            return this.getBukkitView().getTopInventory();
        }
    }
}
