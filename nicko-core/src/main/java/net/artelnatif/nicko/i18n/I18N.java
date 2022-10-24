package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;
import org.apache.commons.lang.LocaleUtils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class I18N {
    private final NickoBukkit instance;
    private final MessageFormat formatter = new MessageFormat("");

    public I18N(NickoBukkit instance) {
        this.instance = instance;
        formatter.setLocale(getLocale());
    }

    private Locale getLocale() {
        try {
            return LocaleUtils.toLocale(instance.getNickoConfig().getLocale());
        } catch (IllegalArgumentException exception) {
            instance.getLogger().severe("Invalid locale provided, defaulting to " + Locale.getDefault().getDisplayName() + ".");
            return Locale.getDefault();
        }
    }

    private ResourceBundle getBundle() {
        return ResourceBundle.getBundle("locale", getLocale());
    }

    public String get(String key, Object... arguments) {
        formatter.applyPattern(getBundle().getString(key));
        return formatter.format(arguments);
    }
}
