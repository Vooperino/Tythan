package com.github.archemedes.tythan.bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.bukkit.events.TythanAsyncDummyRunFuncCommandEvent;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.logging.Level;


public class TythanPacketListener {
    @NotNull @Unmodifiable @Getter private static final String funcDummyCommand = "tythanfunccmd";
    void init() {
        ProtocolManager m = ProtocolLibrary.getProtocolManager();
        m.addPacketListener(new PacketAdapter(TythanBukkit.get(), ListenerPriority.HIGHEST, PacketType.Play.Client.CHAT_COMMAND) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                var p = event.getPacket();
                var cmd = p.getStrings().read(0);
                if (!StringUtils.isEmpty(cmd)) {
                    var args = cmd.split(" ");
                    if (args[0].equalsIgnoreCase(funcDummyCommand)) {
                        String[] newArgs = new String[args.length-1];
                        for (int i=0, k=0;i<args.length;i++) {
                            if (i==0) continue;
                            newArgs[k]=args[i];
                            k++;
                        }
                        try {
                            new TythanAsyncDummyRunFuncCommandEvent(event.getPlayer(),false,newArgs).callEvent();
                        } catch (IllegalStateException ignored) {
                            new TythanAsyncDummyRunFuncCommandEvent(event.getPlayer(),true,newArgs).callEvent();
                        }
                        TythanInstanceProvider.debug(Level.INFO,event.getPlayer().getName()+" ran/executed a function dummy command: "+cmd);
                        event.setCancelled(true);
                    }
                }
            }
        });

    }
}
