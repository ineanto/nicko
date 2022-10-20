package net.artelnatif.nicko.command.sub;

import net.artelnatif.nicko.command.NickoCommand;

public class NickoSubCmd {
    private final NickoCommand nickoCommand;

    public NickoSubCmd(NickoCommand nickoCommand) {
        this.nickoCommand = nickoCommand;
    }

    public NickoCommand getMainCommand() {
        return nickoCommand;
    }
}
