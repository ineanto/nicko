package net.artelnatif.nicko.gui;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.gui.items.common.BackItem;
import net.artelnatif.nicko.gui.items.settings.BungeeCordCyclingItem;
import net.artelnatif.nicko.gui.items.settings.LanguageCyclingItem;
import org.bukkit.entity.Player;

public class SettingsGUI {
    private final Player player;
    private final GUI gui;

    private final String[] structureIngredients = new String[]{
            "# # # # # # # # #",
            "# % % L # T % % #",
            "B # # # # # # # #"
    };

    public SettingsGUI(Player player) {
        if (!NickoBukkit.getInstance().getNickoConfig().isBungeecordSupport()) {
            structureIngredients[1] = structureIngredients[1].replace("T", "#");
        }

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structureIngredients)
                .addIngredient('B', new BackItem(new MainGUI(player).getGUI()))
                .addIngredient('L', new LanguageCyclingItem().get(player))
                .addIngredient('T', new BungeeCordCyclingItem().get(player))
                .build();
        this.player = player;
    }

    public void open() {
        new SimpleWindow(player, "Nicko", gui).show();
    }
}
