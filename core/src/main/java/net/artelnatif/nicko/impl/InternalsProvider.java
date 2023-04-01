package net.artelnatif.nicko.impl;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class InternalsProvider {
    private static final Logger logger = Logger.getLogger("Internals");
    private static Internals internals;

    static {
        try {
            final String packageName = Internals.class.getPackage().getName();
            final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            final String fullClassName = packageName + "." + bukkitVersion;
            final Class<?> clazz = Class.forName(fullClassName);
            internals = (Internals) clazz.getConstructors()[0].newInstance();
            logger.info("Loaded support for " + bukkitVersion);
        } catch (InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 ClassCastException exception) {
            internals = null;
        }
    }

    public static Internals getInternals() {
        return internals;
    }
}
