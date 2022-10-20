package net.artelnatif.nicko.impl;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

public class InternalsProvider {
    private static Internals internals;

    static {
        try {
            final String packageName = Internals.class.getPackage().getName();
            final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            final String fullClassName = packageName + "." + bukkitVersion;
            System.out.println("[DEBUG] packageName = " + packageName);
            System.out.println("[DEBUG] bukkitVersion = " + bukkitVersion);
            System.out.println("[DEBUG] Searching for " + fullClassName + "...");
            internals = (Internals) Class.forName(fullClassName).getConstructors()[0].newInstance();
        } catch (InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 ClassCastException exception) {
            Bukkit.getLogger().log(Level.SEVERE, "Nicko could not find a valid implementation for this server version. Is your server supported?");
        }
    }

    public static Internals getInternals() {
        return internals;
    }
}
