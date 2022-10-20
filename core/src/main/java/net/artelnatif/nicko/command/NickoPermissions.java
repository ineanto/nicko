package net.artelnatif.nicko.command;

import net.artelnatif.nicko.NickoBukkit;

public class NickoPermissions {

    public static final String NICKO_PERMISSION_BASE = "nicko.";
    // TODO: 20/06/2022 i18n
    public static final String NICKO_PERMISSION_MISSING = NickoBukkit.getInstance().getNickoConfig().getPrefix() + "§cMissing permission.";

    public static final class Player {
        public static final class Command {
            public static final String NICKO_PERMISSION_PLAYER_COMMAND_USE = NICKO_PERMISSION_BASE + "use";
        }
    }
}
