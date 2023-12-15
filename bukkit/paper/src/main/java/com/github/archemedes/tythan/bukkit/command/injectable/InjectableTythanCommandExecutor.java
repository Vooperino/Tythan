package com.github.archemedes.tythan.bukkit.command.injectable;

import com.github.archemedes.tythan.bukkit.command.TythanCommandExecutor;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitSender;
import com.github.archemedes.tythan.command.AgnosticExecutor;
import com.github.archemedes.tythan.command.TythanCommand;
import lombok.Getter;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class InjectableTythanCommandExecutor extends Command implements TabExecutor, PluginIdentifiableCommand, TabCompleter {
    private final AgnosticExecutor delegate;
    @Getter private final Plugin plugin;

    public InjectableTythanCommandExecutor(@NotNull TythanCommand command, @NotNull InjectableTythanCommandData data) {
        super(data.getName());
        this.plugin = data.getPlugin();

        this.setLabel(data.getName());
        this.setName(data.getName());

        if (data.getPermission()!=null) this.setPermission(data.getPermission());
        if (data.getDescription()!=null) this.setDescription(data.getDescription());

        if (data.getAliases()!=null) if (data.getAliases().size()!=0) this.setAliases(data.getAliases());
        else this.setAliases(new ArrayList<>());

        this.delegate = new TythanCommandExecutor(command);
    }
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!this.plugin.isEnabled()) {
            throw new CommandException("Cannot execute Tythan Enhanced Bukkit Command '"+commandLabel+"' in plugin "+this.plugin.getName()+"  - plugin is disabled.");
        }
        var bukkitSender = new BukkitSender(sender);
        return this.delegate.onCommand(bukkitSender , getName(), args);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!this.plugin.isEnabled()) {
            throw new CommandException("Cannot execute Tythan Enhanced Bukkit Command '"+command.getLabel()+"' in plugin "+this.plugin.getName()+"  - plugin is disabled.");
        }
        var bukkitSender  = new BukkitSender(sender);
        return this.delegate.onCommand(bukkitSender, getName(), args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        var bukkitSender  = new BukkitSender(sender);
        return this.delegate.onTabComplete(bukkitSender, getName(), args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        var bukkitSender  = new BukkitSender(sender);
        return this.delegate.onTabComplete(bukkitSender, getName(), args);
    }

}
