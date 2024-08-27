package xyz.ineanto.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.language.Language;
import xyz.ineanto.nicko.language.PlayerLanguage;
import xyz.ineanto.nicko.language.LanguageKey;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationTest {
    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        MockBukkit.mock();
        MockBukkit.load(Nicko.class, config);
    }

    @Test
    @DisplayName("Translate Line With Replacement")
    public void translateItemTranslationWithoutLore() {
        final PlayerLanguage playerLanguage = new PlayerLanguage(Language.FRENCH);
        final String translation = playerLanguage.translate(LanguageKey.Event.Settings.ERROR, false, "Test");
        assertEquals("§cImpossible de mettre à jour vos paramètres. §7§o(Test)", translation);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
