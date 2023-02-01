package net.artelnatif.nicko.impl;

import net.artelnatif.nicko.bukkit.NickoBukkit;
import org.bukkit.Bukkit;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public class InternalsProvider {
    private static Internals internals;

    static {
        try {
            final String packageName = Internals.class.getPackage().getName();
            final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            final String fullClassName = packageName + "." + bukkitVersion;
            final Class<?> clazz = Class.forName(fullClassName);
            internals = (Internals) clazz.getConstructors()[0].newInstance();
            for (Annotation annotation : clazz.getDeclaredAnnotations()) {
                if (annotation instanceof Unstable) {
                    NickoBukkit.getInstance().getLogger().warning("Version " + Bukkit.getVersion() + " has been marked as unstable.");
                    NickoBukkit.getInstance().getLogger().warning("Nicko might not work at all until the version has been tested extensively.");
                    NickoBukkit.getInstance().getLogger().warning("Proceed with caution!");
                }
            }
        } catch (InvocationTargetException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 ClassCastException exception) {
            internals = null;
        }
    }

    public static Internals getInternals() {
        return internals;
    }
}
