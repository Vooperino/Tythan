package com.github.archemedes.tythan.bukkit;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.agnostic.Command;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.command.BrigadierProvider;
import com.github.archemedes.tythan.bukkit.command.Commands;
import com.github.archemedes.tythan.bukkit.command.params.BukkitArguments;
import com.github.archemedes.tythan.bukkit.command.params.SenderTypes;
import com.github.archemedes.tythan.bukkit.command.params.TythanArguments;
import com.github.archemedes.tythan.bukkit.menu.MenuListener;
import com.github.archemedes.tythan.bukkit.utils.TythanUtils;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitKyoriComponentBuilder;
import com.github.archemedes.tythan.bukkit.wrapper.Run;
import com.github.archemedes.tythan.command.brigadier.CommandNodeManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;

public class TythanBukkit extends JavaPlugin implements Tythan {

    public static TythanBukkit get() {return (TythanBukkit) Tythan.get();}
    @Getter @Nullable private BukkitTythanVersioning mcVerHandler;

    @Override
    public void onLoad() {
        TythanInstanceProvider.startTythanCore(this);
    }

    @Override
    public void onEnable() {
        this.mcVerHandler = BukkitTythanVersioning.getVersionHandler();
        this.registerCommandParameters();
        new TythanPacketListener().init();
        this.registerEventHandler(new MenuListener());
        if (TythanUtils.brigadierVersionValidation()) {
            Run.as(this).delayed(2L,()->{
                CommandNodeManager.getInstance().inject(BrigadierProvider.get().getBrigadier().getRoot());
            });
        }
    }

    @Override
    public void onDisable() {
        TythanInstanceProvider.stopTythanCore();
    }

    @Override
    public @NotNull @Unmodifiable File getRootDirectory() {
        return this.getDataFolder();
    }

    @Override
    public @NotNull AbstractKyoriComponentBuilder<?> getKyoriComponentBuilder() {
        return new BukkitKyoriComponentBuilder();
    }

    private void registerCommandParameters() {
        SenderTypes.registerCommandSenderType();
        SenderTypes.registerOfflinePlayerType();
        SenderTypes.registerPlayerType();
        BukkitArguments.buildBukkitParameter();
        TythanArguments.buildTythanParameters();
    }

    private void registerEventHandler(@NotNull Listener listener) {
        getServer().getPluginManager().registerEvents(listener,this);
    }

}
