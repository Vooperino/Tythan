package com.github.archemedes.tythan.bukkit.nms;

import com.github.archemedes.tythan.agnostic.CommonKyoriComponentBuilder;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.nms.abstracts.AbstractSkin;
import com.github.archemedes.tythan.bukkit.nms.enums.WrappedChatVisibility;
import com.mojang.authlib.GameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.UUID;

public interface PlayerNMSWrapped {

    default void sendSystemMessage(@NotNull Component message) {
        this.sendSystemMessage(new CommonKyoriComponentBuilder().append(message));
    }
    default void sendSystemMessage(@NotNull String message) {
        this.sendSystemMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(message));
    }

    default boolean isBedrockUser() {
        return this.getUUID().toString().replace("-", "").startsWith("000000");
    }

    default @NotNull Player toBukkitPlayer() {
        if (this.getUUID()==null) throw new NullPointerException("returning player UUID is null or nothing");
        return Bukkit.getPlayer(this.getUUID());
    }
    default void clearSkinProperties() {
        this.updateSkinProperties(this.getDefaultSKin());
    }
    @NotNull @Unmodifiable UUID getUUID();
    @NotNull GameProfile getGameProfile();
    @NotNull AbstractSkin getDefaultSKin();
    @NotNull @Unmodifiable WrappedChatVisibility getChatVisibility();
    void sendSystemMessage(@NotNull AbstractKyoriComponentBuilder message);
    void updateSkinProperties(@NotNull AbstractSkin skin);
    void callInventoryUpdatePacket();
    void sendPacket(Object @NotNull ... packet);
}
