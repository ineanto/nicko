package xyz.ineanto.nicko.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.ineanto.nicko.event.custom.PromptCloseEvent;

public class PromptCloseListener implements Listener {
    @EventHandler
    public void onPromptClose(PromptCloseEvent event) {
        String playerName = event.getPlayer().getName();
        String skin = event.getSkin().orElse("No skin selected");
        String name = event.getName().orElse("No name selected");

        System.out.println("Player " + playerName + " closed the prompt with skin: " + skin + " and name: " + name);
    }
}
