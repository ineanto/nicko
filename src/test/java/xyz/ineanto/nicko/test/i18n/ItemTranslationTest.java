package xyz.ineanto.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;
import xyz.ineanto.nicko.language.Translation;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTranslationTest {
    private static PlayerMock player;

    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        MockBukkit.mock();
        MockBukkit.load(Nicko.class, config);
    }

    @Test
    @DisplayName("Translate Item Without Lore")
    public void translateItemTranslationWithoutLore() {
        final PlayerLanguage playerLanguage = new PlayerLanguage(Language.FRENCH);
        final Translation translation = playerLanguage.translateAndReplace(LanguageKey.GUI.GO_BACK);
        assertTrue(translation.lore().isEmpty());
        assertEquals(translation.name(), "Retour");
    }

    @Test
    @DisplayName("Translate Item")
    public void translateItemLore() {
        final PlayerLanguage playerLanguage = new PlayerLanguage(Language.FRENCH);

        final Translation test = playerLanguage.translateAndReplace(LanguageKey.GUI.Settings.TOGGLEABLE_BUTTON, "EST", "EST");
        test.lore().forEach(System.out::println);

        final Translation translation = playerLanguage.translateAndReplace(LanguageKey.GUI.Admin.Cache.STATISTICS, "1", "1");
        assertFalse(translation.lore().isEmpty());
        assertEquals("Nombre de requêtes: <aqua>1</aqua>", translation.lore().get(0));
        assertEquals("Nb. de skin dans le cache: <aqua>1</aqua>", translation.lore().get(1));
        assertEquals("<dark_gray><i>Le cache est vidé toutes les 24 heures.</i></dark_gray>", translation.lore().get(2));
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
