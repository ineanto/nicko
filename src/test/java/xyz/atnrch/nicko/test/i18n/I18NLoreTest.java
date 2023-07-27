package xyz.atnrch.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.config.Configuration;
import xyz.atnrch.nicko.config.DataSourceConfiguration;
import xyz.atnrch.nicko.i18n.I18N;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.i18n.ItemTranslation;
import xyz.atnrch.nicko.i18n.Locale;

public class I18NLoreTest {
    private static NickoBukkit plugin;
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DataSourceConfiguration.SQL_EMPTY,
                DataSourceConfiguration.REDIS_EMPTY,
                "",
                false);
        final ServerMock server = MockBukkit.mock();
        plugin = MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Translate Item Lore")
    public void translateItemLore() {
        final I18N i18n = new I18N(Locale.FRENCH);
        final ItemTranslation translation = i18n.translateItem(I18NDict.GUI.Home.ADMIN);
        System.out.println("name = " + translation.getName());
        System.out.println("lore = " + translation.getLore());
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
