package com.github.archemedes.tythan.bukkit.nms.v1_19_4;

import com.github.archemedes.tythan.bukkit.nms.TythanBukkitNMS;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class TythanNMSV1_19_4 implements TythanBukkitNMS {
    @Override
    public @NotNull <T> RootCommandNode getDispatcherCommandRoot() {
        return this.getDispatcherCommand().getRoot();
    }

    @Override
    public @NotNull <T> CommandDispatcher getDispatcherCommand() {
        return MinecraftServer.getServer().getCommands().getDispatcher();
    }
}
