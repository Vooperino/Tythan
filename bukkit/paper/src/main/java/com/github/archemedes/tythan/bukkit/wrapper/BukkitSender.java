package com.github.archemedes.tythan.bukkit.wrapper;

import com.github.archemedes.tythan.agnostic.AgnosticObject;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@RequiredArgsConstructor
public class BukkitSender implements Sender, AgnosticObject<CommandSender> {
    @Getter private final CommandSender handle;
    @Override public boolean hasPermission(String perm) {return handle.hasPermission(perm);}
    @Override public String getName() {return handle.getName();}
    @Override public boolean isPlayer() {return this.handle instanceof Player;}
    @Override public void sendMessage(String msg) {handle.sendMessage(msg);}
    @Override public void sendMessage(Component msg) {handle.sendMessage(msg);}

    @Override
    public void sendMessage(AbstractKyoriComponentBuilder msg) {
        this.handle.sendMessage(msg.build());
    }

    @Override public void sendMessage(UUID uuid, String msg) {
        handle.sendMessage(msg);
    }
    @Override public void sendCommand(String command) {
        Bukkit.getServer().dispatchCommand(handle,command);
    }

    @Override public void sendRichMessage(String mini_message) {this.sendMessage(new BukkitKyoriComponentBuilder().appendRich(mini_message));}
    @Override public void sendRichMessage(UUID uuid, String mini_message) {
        this.sendMessage(new BukkitKyoriComponentBuilder().appendRich(mini_message));
    }

    @Override
    public @Nullable Object getPlayer() {
        if (isPlayer()) return Bukkit.getPlayer(this.getUUID());
        return null;
    }


    @Override public void sendMessage(Component... msg) {for(Component c : msg) handle.sendMessage(c);}
    @Override
    public @Nullable UUID getUUID() {
        if (this.handle instanceof Player p) return p.getUniqueId();
        return null;
    }

}
