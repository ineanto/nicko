package xyz.ineanto.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.config.DefaultDataSources;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.i18n.ItemTranslation;
import xyz.ineanto.nicko.i18n.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTranslationTest {
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = new Configuration(
                DefaultDataSources.SQL_EMPTY,
                DefaultDataSources.REDIS_EMPTY,
                "",
                false);
        MockBukkit.mock();
        MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Translate Item Without Lore")
    public void translateItemTranslationWithoutLore() {
        final I18N i18n = new I18N(Locale.FRENCH);
        final ItemTranslation translation = i18n.fetchTranslation(I18NDict.GUI.GO_BACK);
        assertTrue(translation.lore().isEmpty());
        assertEquals(translation.name(), "Retour");
    }

    @Test
    @DisplayName("Translate Item")
    public void translateItemLore() {
        final I18N i18n = new I18N(Locale.FRENCH);
        final ItemTranslation translation = i18n.fetchTranslation(I18NDict.GUI.Admin.Cache.STATISTICS, "1", "1");
        assertFalse(translation.lore().isEmpty());
        assertEquals("§fNombre de requêtes: §b1", translation.lore().get(0));
        assertEquals("§fNb. de skin dans le cache: §b1", translation.lore().get(1));
        assertEquals("§8§oLe cache est vidé toutes les 24 heures.", translation.lore().get(2));
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
