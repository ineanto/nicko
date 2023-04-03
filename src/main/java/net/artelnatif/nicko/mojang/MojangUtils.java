package net.artelnatif.nicko.mojang;

import java.util.UUID;
import java.util.regex.Pattern;

public class MojangUtils {
    public static boolean isUsernameInvalid(String username) {
        return !Pattern.matches("^\\w{3,16}$", username);
    }

    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException {
        if (trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
    }
}
