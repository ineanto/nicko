package net.artelnatif.nicko.bungee;

import net.artelnatif.nicko.bungee.event.UpdateMessageListener;
import net.md_5.bungee.api.plugin.Plugin;

public class NickoBungee extends Plugin {
    public static final String NICKO_PLUGIN_CHANNEL_BASE = "nicko:";
    public static final String PROXY_UPDATE = NICKO_PLUGIN_CHANNEL_BASE + "update";
    public static final String PROXY_FETCH = NICKO_PLUGIN_CHANNEL_BASE + "fetch";
    public static final String SERVER_DATA = NICKO_PLUGIN_CHANNEL_BASE + "data";

    private static NickoBungee plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Registering channel...");
        getProxy().registerChannel(SERVER_DATA);

        getLogger().info("Registering listener...");
        getProxy().getPluginManager().registerListener(this, new UpdateMessageListener());

        getLogger().info("Nicko (Bungee) has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Unregistering channels...");
        getProxy().unregisterChannel(PROXY_UPDATE);

        getLogger().info("Nicko (Bungee) has been disabled.");
    }

    public static NickoBungee getInstance() {
        return plugin;
    }
}
