package net.artelnatif.nicko.impl;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

public class InternalsProvider {
    private static Internals internals;
    private static boolean protocolLib = true;

    static {
        try {
            final String packageName = Internals.class.getPackage().getName();
            final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            String fullClassName = packageName + "." + bukkitVersion;
            if (protocolLib) {
                System.out.println("USING PROTOCOLLIB HACK");
                fullClassName += "_P";
            }
            internals = (Internals) Class.forName(fullClassName).getConstructors()[0].newInstance();
        } catch (InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 ClassCastException exception) {
            internals = null;
        }
    }

    public static Internals getInternals() {
        return internals;
    }
}
