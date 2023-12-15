package com.github.archemedes.tythan.bukkit.convo.chatstream;

import com.github.archemedes.tythan.agnostic.abstracts.AbstractChatStream;
import com.github.archemedes.tythan.bukkit.TythanBukkit;
import com.github.archemedes.tythan.bukkit.wrapper.Run;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

import java.util.function.Consumer;
import java.util.function.Function;


@RequiredArgsConstructor
class PromptListener <T extends Event> implements Consumer<AbstractChatStream.Prompt>, Listener, EventExecutor {
    final UUID uuid;
    final Class<T> clazz;
    final Function<T, Object> function;
    final Function<T, Player> howToGetPlayer;

    AbstractChatStream.Prompt prompt;
    BukkitTask giveUp;

    @Override
    public void accept(AbstractChatStream.Prompt p) {
        prompt = p;
        task();
        var m = Bukkit.getPluginManager();

        m.registerEvent(ChatStreamMessagerEvent.class, this, EventPriority.LOW, this, TythanBukkit.get(), true);
        m.registerEvent(InventoryOpenEvent.class, this, EventPriority.LOW, this, TythanBukkit.get(), true);
        if(clazz != ChatStreamMessagerEvent.class && clazz != InventoryOpenEvent.class)
            m.registerEvent(clazz, this, EventPriority.LOW, this, TythanBukkit.get(), true);
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        if(event instanceof InventoryOpenEvent) {
            InventoryOpenEvent ioe = (InventoryOpenEvent) event;
            if(ioe.getPlayer().getUniqueId().equals(uuid)) ioe.setCancelled(true);
        }else if(event instanceof ChatStreamMessagerEvent e) {
            if(e.getPlayer().getUniqueId().equals(uuid)) {
                e.setCancelled(true);
                if("stop".equalsIgnoreCase(e.getMessage())) {
                    e.getPlayer().sendMessage(prompt.getAbandonMessage());
                    Run.as(TythanBukkit.get()).sync(this::abandon);
                    return;
                }
            }
        }

        if(clazz.isInstance(event)) {
            T theEvent = clazz.cast(event);
            Player who = howToGetPlayer.apply(theEvent);
            if(who != null && who.getUniqueId().equals(uuid)) {
                if (event instanceof ChatStreamMessagerEvent e) e.setCancelled(true);
                task();
                Object result = function.apply(theEvent);
                if(result != null) {
                    close();
                    prompt.fulfil(result);
                } else {
                    prompt.sendPrompt();
                }
            }
        }
    }

    private void task() {
        if(giveUp != null) giveUp.cancel();
        giveUp = Bukkit.getScheduler().runTaskLater(TythanBukkit.get(), ()->{
            giveUp = null;
            abandon();
            Player p = Bukkit.getPlayer(uuid);
            if(p != null) p.sendMessage(prompt.getTimeoutMessage());
        }, 40*20);
    }

    private void close() {
        HandlerList.unregisterAll(this);
        if(giveUp != null) giveUp.cancel();
    }

    private void abandon() {
        close();
        prompt.abandon();
    }

    @Override
    public String toString() {
        return "HeyListen{" + uuid.toString() + "->" + LegacyComponentSerializer.legacyAmpersand().serialize(prompt.getText()) + "}";
    }

}
