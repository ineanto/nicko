package net.artelnatif.nicko.gui.sub;

import de.studiocode.invui.gui.GUI;
import de.studiocode.invui.gui.builder.GUIBuilder;
import de.studiocode.invui.gui.builder.guitype.GUIType;
import de.studiocode.invui.window.impl.single.SimpleWindow;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.gui.MainGUI;
import net.artelnatif.nicko.gui.items.common.BackItem;
import net.artelnatif.nicko.gui.items.settings.BungeeCordCyclingItem;
import net.artelnatif.nicko.gui.items.settings.LanguageCyclingItem;
import org.bukkit.entity.Player;

public class SettingsGUI {
    private final Player player;
    private final GUI gui;
    private final String structure = """
            # # # # # # # # #
            # % % L # T % % #
            B # # # # # # # #
            """;

    public SettingsGUI(Player player) {
        if (!NickoBukkit.getInstance().getNickoConfig().isBungeecordSupport()) {
            final String[] rows = structure.split("\n");
            rows[1] = rows[1].replace("T", "#");
        }

        this.gui = new GUIBuilder<>(GUIType.NORMAL)
                .setStructure(structure)
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
