package com.github.archemedes.tythan.bukkit.convo.chatstream;

import com.github.archemedes.tythan.agnostic.abstracts.AbstractChatStream;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.TythanBukkit;
import com.github.archemedes.tythan.bukkit.TythanPacketListener;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitSender;
import com.github.archemedes.tythan.bukkit.wrapper.Run;
import com.github.archemedes.tythan.utils.TColor;
import com.google.common.base.Predicates;
import io.papermc.paper.event.player.AsyncChatEvent;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Predicate;

public class ChatStream extends AbstractChatStream<ChatStream> {
    @Getter protected Component abandonButton;
    @Getter protected Component abandonMessage;
    @Getter protected Component timeoutMessage;
    @Getter protected boolean includeAbandonButton;

    public ChatStream(Player p) {
        super(new BukkitSender(p), p.getUniqueId());
        this.includeAbandonButton = true;
        this.abandonButton = new BukkitKyoriComponentBuilder().appendButton("X", this.getDummyInvalidCommand("stop"), "Click to exit prompt",TColor.RED,TColor.RED).build();
        this.abandonMessage = new BukkitKyoriComponentBuilder().append("Stop command received. Exiting", TColor.GRAY, TextDecoration.ITALIC).build();
        this.timeoutMessage = new BukkitKyoriComponentBuilder().append("We didn't receive your input in time. Exiting.",TColor.GRAY, TextDecoration.ITALIC).build();
    }

    public <T extends PlayerEvent> ChatStream listen(String contextTag, Component message, Class<T> c, Function<T, Object> listener) {
        return listen(contextTag, message, c, listener, PlayerEvent::getPlayer);
    }

    public <T extends Event> ChatStream listen(String contextTag, Component message, Class<T> c, Function<T, Object> listener, Function<T, Player> howToGetPlayer) {
        prompt(contextTag, message, new PromptListener<>(uuid, c, listener, howToGetPlayer),includeAbandonButton,abandonButton,abandonMessage,timeoutMessage);
        return this;
    }

    @Override
    public ChatStream prompt(String contextTag, Component message, Predicate<String> filter, Function<String, ?> mapper) {
        return listen(contextTag, message, ChatStreamMessagerEvent.class, x->{
            if(filter.test(x.getMessage())) {
                x.setCancelled(true);
                return mapper.apply(x.getMessage());
            }
            else return null;
        });

    }

    public ChatStream clickBlockPrompt(String contextTag, String message) {
        return clickBlockPrompt(contextTag, Component.text(message));
    }

    @Deprecated public ChatStream clickBlockPrompt(String contextTag, String message, Predicate<Block> filter) {
        return clickBlockPrompt(contextTag, Component.text(message), filter);
    }

    public ChatStream clickBlockPrompt(String contextTag, Component message) {
        return clickBlockPrompt(contextTag, message, Predicates.alwaysTrue());
    }

    public ChatStream clickBlockPrompt(String contextTag, Component message, Predicate<Block> filter) {
        return listen(contextTag, message, PlayerInteractEvent.class, e->{
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Block b = e.getClickedBlock();
                e.setCancelled(true);
                if(filter.test(b)) return b;
            }
            return null;
        });
    }

    @Deprecated public ChatStream clickEntityPrompt(String contextTag, String message) {
        return clickEntityPrompt(contextTag, Component.text(message));
    }

    @Deprecated public ChatStream clickEntityPrompt(String contextTag, String message, Predicate<Entity> filter) {
        return clickEntityPrompt(contextTag, Component.text(message), filter);
    }

    public ChatStream clickEntityPrompt(String contextTag, Component message) {
        return clickEntityPrompt(contextTag, message, Predicates.alwaysTrue());
    }

    public ChatStream clickEntityPrompt(String contextTag, Component message, Predicate<Entity> filter) {
        return listen(contextTag, message, PlayerInteractEntityEvent.class, e->{
            Entity entity = e.getRightClicked();
            e.setCancelled(true);
            if(filter.test(entity)) return entity;
            return null;
        });
    }

    public ChatStream dontIncludeAbandonButton() {
        this.includeAbandonButton = false;
        getThis().deludeAbandonButton();
        return getThis();
    }

    public ChatStream setAbandonMessage(String message){return setTimeoutMessage(Component.text(message,TColor.WHITE));}
    public ChatStream setAbandonMessage(AbstractKyoriComponentBuilder builder) {return setTimeoutMessage(builder.build());}
    public ChatStream setAbandonMessage(Component message) {
        this.abandonMessage = message;
        getThis().abandonMessage(message);
        return getThis();
    }

    public ChatStream setTimeoutMessage(String message){return setTimeoutMessage(Component.text(message,TColor.WHITE));}
    public ChatStream setTimeoutMessage(AbstractKyoriComponentBuilder builder) {return setTimeoutMessage(builder.build());}
    public ChatStream setTimeoutMessage(Component message) {
        this.timeoutMessage = message;
        getThis().timeoutMessage(message);
        return getThis();
    }

    public ChatStream setAbandonButton(AbstractKyoriComponentBuilder builder) {return setAbandonButton(builder.build());}
    public ChatStream setAbandonButton(Component button) {
        this.abandonButton = button;
        getThis().abandonButton(button);
        return getThis();
    }


    @Override
    protected ChatStream getThis() {
        return this;
    }

    @Override
    protected void resolveFinishedStream() {
        if(Bukkit.isPrimaryThread()) onActivate.accept(context);
        else Run.as(TythanBukkit.get()).sync(()->onActivate.accept(context));
    }

    @Override
    protected void resolveAbaondonedStream() {
        if (onAbandon == null) return;
        if(Bukkit.isPrimaryThread()) onAbandon.accept(context);
        else Run.as(TythanBukkit.get()).sync(()->onAbandon.accept(context));
    }

    @Contract(pure = true)
    public final @NotNull String getDummyInvalidCommand(@NotNull String input) {
        return "/"+ TythanPacketListener.getFuncDummyCommand()+" "+input;
    }

}
