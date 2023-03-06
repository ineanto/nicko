package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.gui.items.common.GoBack;
import net.artelnatif.nicko.gui.items.settings.BungeeCordCycling;
import net.artelnatif.nicko.gui.items.settings.LanguageCycling;
import net.artelnatif.nicko.gui.items.settings.OptionUnavailable;
import org.bukkit.entity.Player;

public class SettingsGUI {
    private final Player player;
    private final GUI gui;

    public SettingsGUI(Player player) {
        final String[] dynamicStructure = new String[]{
                "# # # # # # # # #",
                "# % % L U T % % #",
                "B # # # # # # # #"
        };

        // TODO: 3/6/23 Replace when Redis is not enabled
        dynamicStructure[1] = dynamicStructure[1].replace("T", "U");

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(dynamicStructure)
                .addIngredient('B', new GoBack(new MainGUI(player).getGUI()))
                .addIngredient('L', new LanguageCycling().get(player))
                .addIngredient('T', new BungeeCordCycling().get(player))
                .addIngredient('U', new OptionUnavailable())
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}