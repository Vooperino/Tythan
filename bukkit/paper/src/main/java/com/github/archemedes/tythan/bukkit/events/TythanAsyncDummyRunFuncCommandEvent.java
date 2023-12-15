package com.github.archemedes.tythan.bukkit.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class TythanAsyncDummyRunFuncCommandEvent extends PlayerEvent {
    @Getter @NotNull @Unmodifiable private final String[] arguments;

    public TythanAsyncDummyRunFuncCommandEvent(@NotNull Player who, boolean async, @NotNull final String[] arguments) {
        super(who, async);
        this.arguments = arguments;
    }

    @NotNull public String covertToString() {
        if (this.arguments.length==0) return "";
        StringBuilder sb = new StringBuilder();
        for (String str : this.arguments) {
            sb.append(str+" ");
        }
        return sb.substring(0,sb.length()-1);
    }

    private static final HandlerList handlerList = new HandlerList();
    @NotNull public HandlerList getHandlers() {return handlerList;}
    public static HandlerList getHandlerList() {return handlerList;}

}
