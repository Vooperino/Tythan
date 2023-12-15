package com.github.archemedes.tythan.bukkit.command.injectable;

import com.github.archemedes.tythan.Tythan;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public final class InjectionUtils {
    public static void registerCommand(@NotNull InjectableTythanCommandData command, @NotNull InjectableTythanCommandExecutor exec, boolean forceInject) {
        Tythan.get().getLogger().warning("Attempting to unregister bukkit/paper command "+command.getName()+" for re-injection!");
        var cmd = Bukkit.getServer().getCommandMap().getCommand(command.getName());
        var pcmd = Bukkit.getPluginCommand(command.getName());
        if (pcmd!=null) {
            if (forceInject) {
                pcmd.unregister(Bukkit.getCommandMap());
                Bukkit.getCommandMap().getKnownCommands().remove(pcmd.getLabel());
                Bukkit.getCommandMap().getKnownCommands().remove(pcmd.getName());
                exec.register(Bukkit.getCommandMap());
                Bukkit.getCommandMap().getKnownCommands().put(command.getName(),exec);
            }
        } else if (cmd!=null) {
            if (forceInject) {
                for (String alias : cmd.getAliases()) {
                    var aliasCMD = Bukkit.getCommandMap().getCommand(alias);
                    if (aliasCMD!=null) {
                        Bukkit.getCommandMap().getKnownCommands().remove(aliasCMD);
                        aliasCMD.unregister(Bukkit.getCommandMap());
                    }
                }
                Bukkit.getCommandMap().getKnownCommands().remove(command.getName());
                cmd.unregister(Bukkit.getCommandMap());
                Bukkit.getCommandMap().getKnownCommands().put(command.getName(),exec);
            } else Tythan.get().getLogger().warning("Bukkit/paper command "+command.getName()+" seems to be already registered, but still attempting to registering, but there no promises if it will work!");
        }
        Bukkit.getServer().getCommandMap().register(command.getName(),command.getPlugin().getPluginMeta().getName(),exec);

    }
}
