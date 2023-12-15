package com.github.archemedes.tythan.bukkit.command.params;

import com.github.archemedes.tythan.bukkit.command.Commands;
import com.github.archemedes.tythan.bukkit.interfaces.MinecraftProfile;
import com.github.archemedes.tythan.utils.TColor;
import com.github.archemedes.tythan.utils.mojang.MojangCommunicator;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;
import java.util.regex.Pattern;


@UtilityClass
public class TythanArguments {
    public static void buildTythanParameters() {
        Commands.defineArgumentType(TColor.class)
                .defaultName("Tythan Color")
                .defaultError("Invalid hex color or invalid input!")
                .mapper(i->{
                    var hex = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
                    if (hex.matcher(i).matches()) return new TColor(i);
                    return null;
                })
                .register();

        Commands.defineArgumentType(MinecraftProfile.class)
                .defaultName("Minecraft Profile")
                .defaultError("Could not find minecraft profile by the username entered")
                .mapper((i)->{
                    boolean isUUID = false;
                    @Nullable UUID uuid = null;
                    try {
                        uuid = UUID.fromString(i);
                        isUUID = true;
                    } catch (IllegalArgumentException ignored){}

                    OfflinePlayer player = null;
                    if (isUUID) player = Bukkit.getOfflinePlayer(uuid);
                    else player = Bukkit.getOfflinePlayerIfCached(i);
                    if (player!=null) {
                        OfflinePlayer finalPlayer = player;
                        return new MinecraftProfile() {
                            @Override public @NotNull @Unmodifiable UUID getUUID() {return finalPlayer.getUniqueId();}
                            @Override public @Nullable @Unmodifiable OfflinePlayer toOfflinePlayer() {return finalPlayer;}
                        };
                    }
                    try {
                        if (isUUID) {
                            @Nullable UUID finalUuid = uuid;
                            return new MinecraftProfile() {
                                @Override public @NotNull @Unmodifiable UUID getUUID() {return finalUuid;}
                                @Override public @Nullable @Unmodifiable OfflinePlayer toOfflinePlayer() {return Bukkit.getOfflinePlayer(finalUuid);}
                            };
                        }
                        var r_uuid = MojangCommunicator.requestPlayerUUID(i);
                        if (r_uuid!=null) {
                            return new MinecraftProfile() {
                                @Override public @NotNull @Unmodifiable UUID getUUID() {return r_uuid;}
                                @Override public @Nullable @Unmodifiable OfflinePlayer toOfflinePlayer() {return Bukkit.getOfflinePlayer(r_uuid);}
                            };
                        }
                    } catch (Exception ignored) {}
                    return null;
                })
                .completer(SenderTypes.PLAYER_COMPLETER)
                .register();

    }
}
