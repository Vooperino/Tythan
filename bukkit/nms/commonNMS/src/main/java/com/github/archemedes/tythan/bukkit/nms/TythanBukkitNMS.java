package com.github.archemedes.tythan.bukkit.nms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import org.jetbrains.annotations.NotNull;

public interface TythanBukkitNMS {
    @NotNull <T> RootCommandNode getDispatcherCommandRoot();
    @NotNull <T> CommandDispatcher getDispatcherCommand();
}
