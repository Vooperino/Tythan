package com.github.archemedes.tythan.bukkit.nms;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface TythanBukkitNMS {
    @Nullable default PlayerNMSWrapped getNMSWrappedPlayer(@NotNull OfflinePlayer player) {
        return this.getNMSWrappedPlayer(player.getUniqueId());
    }
    @Nullable default PlayerNMSWrapped getNMSWrappedPlayer(@NotNull String username) {
        @Nullable var p = Bukkit.getPlayer(username);
        if (p==null) return null;
        return this.getNMSWrappedPlayer(p);
    }
    default void deInitNMS(){}

    @NotNull <T> RootCommandNode getDispatcherCommandRoot();
    @NotNull <T> CommandDispatcher getDispatcherCommand();
    @Nullable PlayerNMSWrapped getNMSWrappedPlayer(@NotNull UUID id);

    void initNMS(@NotNull Plugin bukkitTythan);

}
