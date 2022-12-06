package net.artelnatif.nicko.bungee.event;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;

public class PluginMessageListener implements Listener {
    @EventHandler
    public void onSwitch(PluginMessageEvent event) {
        final String message = Arrays.toString(event.getData());
        System.out.println("message = " + message);
    }
}
