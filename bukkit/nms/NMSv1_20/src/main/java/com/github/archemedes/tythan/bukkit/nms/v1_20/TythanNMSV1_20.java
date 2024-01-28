package com.github.archemedes.tythan.bukkit.nms.v1_20;

import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.bukkit.nms.PlayerNMSWrapped;
import com.github.archemedes.tythan.bukkit.nms.TythanBukkitNMS;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.MinecraftServer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.logging.Level;

public class TythanNMSV1_20 implements TythanBukkitNMS {

    @NotNull private Plugin bukkitTythan;

    @Override
    public @NotNull <T> RootCommandNode getDispatcherCommandRoot() {
        return this.getDispatcherCommand().getRoot();
    }

    @Override
    public @NotNull <T> CommandDispatcher getDispatcherCommand() {
        return MinecraftServer.getServer().getCommands().getDispatcher();
    }

    @Override
    public @Nullable PlayerNMSWrapped getNMSWrappedPlayer(@NotNull UUID id) {
        var nmsPlayer = MinecraftServer.getServer().getPlayerList().getPlayer(id);
        if (nmsPlayer==null) return null;
        return new WrappedNMSPlayerBuilder(nmsPlayer,this.bukkitTythan);
    }

    @Override
    public void initNMS(@NotNull Plugin bukkitTythan) {
        this.bukkitTythan = bukkitTythan;
        TythanInstanceProvider.debug(Level.INFO,"Tythan NMS Wrapper for MC Version v1_20_R1");
    }
}
