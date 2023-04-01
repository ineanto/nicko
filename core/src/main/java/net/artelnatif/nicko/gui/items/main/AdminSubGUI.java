package net.artelnatif.nicko.gui.items.main;

import net.artelnatif.nicko.gui.AdminGUI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SuppliedItem;

public class AdminSubGUI extends SuppliedItem {
    public AdminSubGUI() {
        super(() -> {
            final ItemBuilder builder = new ItemBuilder(Material.COMMAND_BLOCK);
            builder.addEnchantment(Enchantment.DAMAGE_ALL, 1, false);
            builder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            builder.setDisplayName("§cAdministration panel...");
            builder.addLoreLines("§7Access the administration panel.");
            return builder;
        }, click -> {
            final ClickType clickType = click.getClickType();
            if (clickType.isLeftClick() || clickType.isRightClick()) {
                click.getEvent().getView().close();
                new AdminGUI(click.getPlayer()).open();
                return true;
            }
            return false;
        });
    }
}
