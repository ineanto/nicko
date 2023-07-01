package xyz.atnrch.nicko.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.confirm.ActionCallback;
import xyz.atnrch.nicko.gui.items.confirm.Cancel;
import xyz.atnrch.nicko.gui.items.confirm.Confirm;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class ConfirmGUI {
    private final Player player;
    private final Gui gui;

    public ConfirmGUI(Player player, ActionCallback callback) {
        this.gui = Gui.normal()
                .setStructure(
                        "@ @ @ @ % & & & &",
                        "@ @ @ @ I & & & &",
                        "@ @ @ @ % & & & &"
                )
                .addIngredient('@', new Confirm(callback))
                .addIngredient('&', new Cancel(callback))
                .addIngredient('I', new SimpleItem(new ItemBuilder(Material.PAPER).setDisplayName("§6Select an option").get()))
                .build();
        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle("... > Invalidate > Confirm").open(player);
    }
}
