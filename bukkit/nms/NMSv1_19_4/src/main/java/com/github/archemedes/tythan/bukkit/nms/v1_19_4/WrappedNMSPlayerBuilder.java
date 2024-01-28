package com.github.archemedes.tythan.bukkit.nms.v1_19_4;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.nms.PlayerNMSWrapped;
import com.github.archemedes.tythan.bukkit.nms.abstracts.AbstractSkin;
import com.github.archemedes.tythan.bukkit.nms.enums.WrappedChatVisibility;
import com.github.archemedes.tythan.utils.mojang.MojangCommunicator;
import com.github.archemedes.tythan.utils.mojang.SkinLayerOption;
import com.mojang.authlib.GameProfile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.biome.BiomeManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

@RequiredArgsConstructor
public class WrappedNMSPlayerBuilder implements PlayerNMSWrapped {

    @NotNull private final ServerPlayer player;
    @NotNull private final Plugin bukkitTythan;

    @Override
    public @NotNull @Unmodifiable UUID getUUID() {
        return this.player.getUUID();
    }

    @Override
    public @NotNull GameProfile getGameProfile() {
        return this.player.getGameProfile();
    }

    @Override
    public @NotNull AbstractSkin getDefaultSKin() {
        var defSkin = MojangCommunicator.requestSkinData(this.player.getUUID().toString());
        return new AbstractSkin() {
            @Override public @NotNull @Unmodifiable String getValue() {return defSkin.getValue();}
            @Override public @NotNull @Unmodifiable String getSignature() {return defSkin.getSignature();}
        };
    }

    @Override
    public @NotNull @Unmodifiable WrappedChatVisibility getChatVisibility() {
        var nsmChat = this.player.getChatVisibility();
        switch (nsmChat) {
            case FULL -> {return WrappedChatVisibility.FULL;}
            case SYSTEM -> {return WrappedChatVisibility.SYSTEM;}
            case HIDDEN -> {return WrappedChatVisibility.HIDDEN;}
            default -> {return WrappedChatVisibility.UNKNOWN;}
        }
    }

    @Override
    public void sendSystemMessage(@NotNull AbstractKyoriComponentBuilder message) {
        this.player.sendSystemMessage(Component.Serializer.fromJson(message.toGson()));
    }

    @Override @SneakyThrows
    public void updateSkinProperties(@NotNull AbstractSkin skin) {
        GameProfile profile = this.player.getBukkitEntity().getProfile();
        TythanInstanceProvider.getDebugLogger().log(Level.INFO,"Attempting to update skin properties for "+this.getUUID()+"\nSkin Value: "+skin.getValue()+"\n Skin Signature: "+skin.getSignature());
        if (this.player.getBukkitEntity().getVehicle()!=null) this.player.getBukkitEntity().getVehicle().eject();
        var property = skin.getProperty();
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures",property);
        var a = CompletableFuture.supplyAsync(()->{
            ClientboundPlayerInfoRemovePacket removePlayer = new ClientboundPlayerInfoRemovePacket(List.of(this.getUUID()));
            ClientboundPlayerInfoUpdatePacket addPlayer = ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(this.player));
            ServerLevel world = this.player.getLevel();
            ServerPlayerGameMode gamemode = this.player.gameMode;
            ClientboundRespawnPacket respawn = new ClientboundRespawnPacket(
                    world.dimensionTypeId(),
                    world.dimension(),
                    BiomeManager.obfuscateSeed(world.getSeed()),
                    gamemode.getGameModeForPlayer(),
                    gamemode.getPreviousGameModeForPlayer(),
                    world.isDebug(),
                    world.isFlat(),
                    (byte) 3,
                    this.player.getLastDeathLocation()
            );
            Location l = this.player.getBukkitEntity().getLocation();
            ClientboundPlayerPositionPacket pos = new ClientboundPlayerPositionPacket(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<>(), 0);
            ClientboundSetCarriedItemPacket slot = new ClientboundSetCarriedItemPacket(this.player.getBukkitEntity().getInventory().getHeldItemSlot());
            sendPacket(removePlayer);
            sendPacket(addPlayer);
            sendPacket(respawn);
            SynchedEntityData synchedEntityData = this.player.getEntityData();
            EntityDataAccessor<Byte> entityDataAccessor;
            synchedEntityData.set(entityDataAccessor = new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), new SkinLayerOption(true,true,true,true,true,true,true).toBytesFlags());
            synchedEntityData.markDirty(entityDataAccessor);
            synchedEntityData.refresh(this.player);
            this.player.onUpdateAbilities();
            sendPacket(pos);
            sendPacket(slot);
            this.player.getBukkitEntity().updateScaledHealth();
            this.player.resetSentInfo();
            this.callInventoryUpdatePacket();
            return true;
        });
        var res = a.get();
        Bukkit.getOnlinePlayers().stream().filter(Objects::nonNull).filter(p->p.canSee(this.player.getBukkitEntity())).forEach(p->{
            Bukkit.getScheduler().runTask(bukkitTythan,()->{
                p.hidePlayer(bukkitTythan,this.player.getBukkitEntity());
                p.showPlayer(bukkitTythan,this.player.getBukkitEntity());
            });
        });
    }

    @Override
    public void callInventoryUpdatePacket() {
        this.player.containerMenu.sendAllDataToRemote();
    }

    @Override
    public void sendPacket(Object @NotNull ... packet) {
        for (Object p : packet) this.player.connection.send((Packet<?>) p);
    }
}
