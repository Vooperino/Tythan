package com.github.archemedes.tythan.bukkit.command.injectable;

import com.github.archemedes.tythan.agnostic.Command;
import com.github.archemedes.tythan.agnostic.PluginOwned;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor @Getter
public class InjectableTythanCommandData implements Command, PluginOwned<Plugin>, PluginIdentifiableCommand {
    Plugin plugin;
    String name, permission, description;
    @Setter List<String> aliases;

    public InjectableTythanCommandData(Plugin plugin, String name) {
        this(plugin, name, null);
    }
    public InjectableTythanCommandData(Plugin plugin, String name, String permission) {
        this(plugin, name, permission, null);
    }
    public InjectableTythanCommandData(Plugin plugin, String name, String permission, String description) {
        this(plugin, name, permission, description, new ArrayList<>());
    }
}
