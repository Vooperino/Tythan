package com.github.archemedes.tythan.bukkit.convo.chatstream;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class ChatStreamMessagerEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    @NotNull public HandlerList getHandlers() {return handlerList;}
    public static HandlerList getHandlerList() {return handlerList;}

    @Getter @NotNull @Unmodifiable private String message;

    @Getter @Setter private boolean cancelled;

    public ChatStreamMessagerEvent(@NotNull Player who, boolean async, @NotNull String message) {
        super(who, async);
        this.message = message;
    }

}
