package com.github.archemedes.tythan.bukkit.interfaces;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;

public interface MinecraftProfile {
    @NotNull @Unmodifiable UUID getUUID();
    @Nullable @Unmodifiable OfflinePlayer toOfflinePlayer();
}
