package net.artelnatif.nicko.bungee;

import net.artelnatif.nicko.bungee.event.ServerSwitchListener;
import net.md_5.bungee.api.plugin.Plugin;

public class NickoBungee extends Plugin {
    public static final String NICKO_PLUGIN_CHANNEL_BASE = "nicko:";
    public static final String NICKO_PLUGIN_CHANNEL_FETCH = NICKO_PLUGIN_CHANNEL_BASE + "fetch";
    public static final String NICKO_PLUGIN_CHANNEL_UPDATE = NICKO_PLUGIN_CHANNEL_BASE + "update";

    private static NickoBungee plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Registering channels...");
        getProxy().registerChannel(NICKO_PLUGIN_CHANNEL_FETCH);
        getProxy().registerChannel(NICKO_PLUGIN_CHANNEL_UPDATE);

        getLogger().info("Registering listeners...");
        getProxy().getPluginManager().registerListener(this, new ServerSwitchListener());

        getLogger().info("Nicko (Bungee) has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unregistering channels...");
        getProxy().unregisterChannel(NICKO_PLUGIN_CHANNEL_FETCH);
        getProxy().unregisterChannel(NICKO_PLUGIN_CHANNEL_UPDATE);

        getLogger().info("Nicko (Bungee) has been disabled.");
    }

    public static NickoBungee getInstance() {
        return plugin;
    }
}
