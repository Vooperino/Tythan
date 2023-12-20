package com.github.archemedes.tythan.bukkit.command;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.bukkit.TythanBukkit;
import com.github.archemedes.tythan.bukkit.command.injectable.InjectableCommand;
import com.github.archemedes.tythan.bukkit.command.injectable.InjectableTythanCommandExecutor;
import com.github.archemedes.tythan.bukkit.command.injectable.InjectionUtils;
import com.github.archemedes.tythan.bukkit.utils.TythanUtils;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitCommand;
import com.github.archemedes.tythan.command.*;
import com.github.archemedes.tythan.command.brigadier.CommandNodeManager;
import com.github.archemedes.tythan.command.brigadier.Kommandant;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;
import java.util.logging.Level;

public class Commands {
    public static TythanCommandBuilder builder(@NotNull InjectableCommand command, boolean forceInject) {
        Supplier<TythanCommandTemplate> template = command::toTemplate;
        var wrapper = command.toInjectableData();
        return new AnnotatedCommandParser(template, wrapper).invokeParse((handler)->{
            Tythan.get().getLogger().info("Attempting to inject bukkit/paper command "+wrapper.getName()+" for "+wrapper.getPlugin().getName()+" plugin!");
            var exec = new InjectableTythanCommandExecutor(handler,wrapper);
            InjectionUtils.registerCommand(wrapper,exec,forceInject);
            validatePermission(wrapper.getPermission());
        });
    }

    public static TythanCommandBuilder builder(PluginCommand command, Supplier<TythanCommandTemplate> template) {
        var wrapper = new BukkitCommand(command);
        return new AnnotatedCommandParser(template, wrapper).invokeParse((handler)->{
            if (template.getClass().getAnnotation(Deprecated.class)!=null) {
                Tythan.get().getLogger().warning("(Commander) "+command.getName()+" command is marked as deprecated! Please take a look the commands code! " +
                        "Issue is coming from "+
                        command.getPlugin().getDescription().getName()+
                        " "+command.getPlugin().getDescription().getVersion());
            }
            var pluginCommand = (wrapper).getHandle();
            TythanCommandExecutor executor = new TythanCommandExecutor(handler);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
            pluginCommand.setExecutor(executor);
            validatePermission(handler.getPermission());
            Kommandant kommandant = new TythanBukkitKommandant(handler);
            if (!TythanBukkit.get().getMcVerHandler().isBrokenBrigadier()) kommandant.addBrigadier();
            CommandNodeManager.getInstance().register(kommandant);
            if (!TythanBukkit.get().getMcVerHandler().isBrokenBrigadier()) CommandNodeManager.getInstance().inject(BrigadierProvider.get().getBrigadier().getRoot());
        });

    }

    public static void build(@NotNull InjectableCommand command) {
        build(command,false);
    }

    public static void build(@NotNull InjectableCommand command, boolean forceInject) {
        builder(command,forceInject).build();
    }

    public static void build(String command, Supplier<TythanCommandTemplate> template) {
        build(Bukkit.getPluginCommand(command),template);
    }
    public static void build(PluginCommand command, Supplier<TythanCommandTemplate> template) {
        if(command==null) throw new NullPointerException("Command was not found with in plugin.yml! Suggestion: Try using InjectableCommand");
        builder(command, template).build();
    }

    @Contract("_ -> new")
    public static <T> @NotNull ParameterType<T> defineArgumentType(Class<T> forClass){
        return new ParameterType<>(forClass);
    }

    private static void validatePermission(@Nullable String permission) {
        if (permission==null) return;
        if (StringUtils.isEmpty(permission)) return;
        try {
            TythanInstanceProvider.debug(Level.INFO,"Attempting to register a command permission "+(permission)+" to the server!");
            Bukkit.getServer().getPluginManager().addPermission(new Permission(permission));
        } catch (Exception e) {
            TythanInstanceProvider.debug(Level.WARNING,permission+" is already registered on the server!");
        }

    }

}
