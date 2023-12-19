package xyz.ineanto.nicko.test.i18n;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.ineanto.nicko.NickoBukkit;
import xyz.ineanto.nicko.config.Configuration;
import xyz.ineanto.nicko.i18n.I18N;
import xyz.ineanto.nicko.i18n.I18NDict;
import xyz.ineanto.nicko.i18n.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TranslationTest {
    @BeforeAll
    public static void setup() {
        final Configuration config = Configuration.DEFAULT;
        MockBukkit.mock();
        MockBukkit.load(NickoBukkit.class, config);
    }

    @Test
    @DisplayName("Translate Line With Replacement")
    public void translateItemTranslationWithoutLore() {
        final I18N i18n = new I18N(Locale.FRENCH);
        final String translation = i18n.translatePrefixless(I18NDict.Event.Settings.ERROR, "Test");
        assertEquals("§cImpossible de mettre à jour vos paramètres. §7§o(Test)", translation);
    }

    @AfterAll
    public static void shutdown() {
        MockBukkit.unmock();
    }
}
