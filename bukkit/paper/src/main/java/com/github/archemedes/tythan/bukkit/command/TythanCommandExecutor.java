package com.github.archemedes.tythan.bukkit.command;

import com.github.archemedes.tythan.bukkit.wrapper.BukkitSender;
import com.github.archemedes.tythan.command.AgnosticExecutor;
import com.github.archemedes.tythan.command.TythanCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class TythanCommandExecutor extends AgnosticExecutor implements TabExecutor {
    public TythanCommandExecutor(TythanCommand rootCommand) {
        super(rootCommand);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        BukkitSender bukkitSender = new BukkitSender(sender);
        return super.onTabComplete(bukkitSender, alias, args);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BukkitSender bukkitSender = new BukkitSender(sender);
        return super.onCommand(bukkitSender, label, args);
    }

}
