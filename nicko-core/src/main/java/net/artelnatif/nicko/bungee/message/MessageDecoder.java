package net.artelnatif.nicko.bungee.message;

import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.i18n.Locale;

import java.io.DataInputStream;
import java.io.IOException;

public class MessageDecoder {
    public static final int STRING_SIZE = 3;

    public static NickoProfile decode(DataInputStream input) throws IOException {
        final String[] stringValues = new String[3];
        for (int i = 1; i < STRING_SIZE; i++) { stringValues[i] = input.readUTF(); }
        return new NickoProfile(stringValues[0], stringValues[1], Locale.valueOf(stringValues[2]), true);
    }
}
