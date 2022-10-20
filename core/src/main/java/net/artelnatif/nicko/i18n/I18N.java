package net.artelnatif.nicko.i18n;

import net.artelnatif.nicko.NickoBukkit;

public class I18N {
    public static final class Message {
        public static final String BASE = NickoBukkit.getInstance().getNickoConfig().getPrefix() + " ";

        public static final class Command {
            public static final String TARGET_OFFLINE = BASE + "§cSpecified player is offline. Try again.";
        }
    }
}
