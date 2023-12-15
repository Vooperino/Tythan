package com.github.archemedes.tythan.bukkit.wrapper;

import com.github.archemedes.tythan.agnostic.AgnosticObject;
import com.github.archemedes.tythan.agnostic.Command;
import com.github.archemedes.tythan.agnostic.PluginOwned;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

@Getter @RequiredArgsConstructor
public class BukkitCommand implements AgnosticObject<PluginCommand>, Command, PluginOwned<Plugin> {

    @Delegate(types=Command.class,excludes= CommandSender.Spigot.class)
    private final PluginCommand handle;

    @Override
    public Plugin getPlugin() {
        return handle.getPlugin();
    }

}
