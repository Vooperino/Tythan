package com.github.archemedes.tythan.bukkit.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.bukkit.TythanBukkit;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandsPacketIntercept {
    private final List<CommandNode<Object>> nodes = Collections.synchronizedList(new ArrayList<>());
    RootCommandNode<?> lastSeen = null;

    public void injectNode(CommandNode<Object> node) {
        nodes.add(node);
    }

    public void startListening() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(TythanBukkit.get(), PacketType.Play.Server.COMMANDS) {
                    @Override
                    public void onPacketSending(final PacketEvent event) {
                        @SuppressWarnings("unchecked")
                        var root = (RootCommandNode<Object>) event.getPacket().getModifier().read(0);

                        if(lastSeen == root) Tythan.get().getLogger().severe("ITS A SINGLETON!");
                        lastSeen = root;

                        for(var node : nodes) {
                            String name = node.getName();
                            despigot(root, name);
                            root.addChild(node);
                        }
                    }
                });
    }

    private void despigot(RootCommandNode<Object> root, String alias) {
        var iter = root.getChildren().iterator();
        while(iter.hasNext()) {
            var kid = iter.next(); //Search spigot's attempt at registering the argument
            if(kid.getName().equals(alias)) iter.remove(); //Killing the skeletal framework of spigot gives us full Brigadier power
        }
    }

}
