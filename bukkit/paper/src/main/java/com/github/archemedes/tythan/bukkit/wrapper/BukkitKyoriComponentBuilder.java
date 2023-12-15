package com.github.archemedes.tythan.bukkit.wrapper;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BukkitKyoriComponentBuilder extends AbstractKyoriComponentBuilder<BukkitKyoriComponentBuilder> {
    public BukkitKyoriComponentBuilder() {super("");}
    public BukkitKyoriComponentBuilder(String initial) {super(initial);}
    @Override
    protected BukkitKyoriComponentBuilder getThis() {
        return this;
    }

    public WrappedChatComponent toWrappedChatComponent() {
        return WrappedChatComponent.fromJson(this.toGson());
    }

    public void sendToPlayerAsPacket(@NotNull Player player) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.SYSTEM_CHAT);
        packet.getStrings().write(0,this.toWrappedChatComponent().getJson());
        manager.sendServerPacket(player,packet);
    }
}