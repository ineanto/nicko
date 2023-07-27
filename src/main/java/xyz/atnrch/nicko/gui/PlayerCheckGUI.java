package xyz.atnrch.nicko.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.atnrch.nicko.gui.items.admin.check.PlayerInformationItem;
import xyz.atnrch.nicko.gui.items.common.GoBackItem;
import xyz.atnrch.nicko.gui.items.common.ScrollDownItem;
import xyz.atnrch.nicko.gui.items.common.ScrollUpItem;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.ScrollGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerCheckGUI {
    public static final String TITLE = "Nicko > Admin... > Check";

    private final Player player;
    private final Gui gui;

    public PlayerCheckGUI(Player player) {
        final List<Item> items = Bukkit.getOnlinePlayers().stream()
                .map(Entity::getUniqueId)
                .map(PlayerInformationItem::new)
                .collect(Collectors.toList());

        final AdminGUI parent = new AdminGUI(player);
        final GoBackItem backItem = new GoBackItem(player);

        gui = ScrollGui.items(guiItemBuilder -> {
            guiItemBuilder.setStructure(
                    "x x x x x x x x U",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x #",
                    "x x x x x x x x D",
                    "B % % % % % % % %");
            guiItemBuilder.addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL);
            guiItemBuilder.addIngredient('U', new ScrollUpItem());
            guiItemBuilder.addIngredient('D', new ScrollDownItem());
            guiItemBuilder.addIngredient('B', backItem.get(parent.getGUI(), parent.getTitle()));
            guiItemBuilder.setContent(items);
        });

        this.player = player;
    }

    public void open() {
        Window.single().setGui(gui).setTitle(TITLE).open(player);
    }
}
