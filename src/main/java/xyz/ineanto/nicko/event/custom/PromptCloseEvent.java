package xyz.ineanto.nicko.event.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PromptCloseEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private boolean isCancelled;
    private final Player player;
    private final String skin, name;

    public PromptCloseEvent(Player player, String skin, String name) {
        this.player = player;
        this.skin = skin;
        this.name = name;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public static HandlerList getHandlerList() { return HANDLERS_LIST; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public Player getPlayer() {
        return player;
    }

    public Optional<String> getSkin() {
        return Optional.ofNullable(skin);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
}
