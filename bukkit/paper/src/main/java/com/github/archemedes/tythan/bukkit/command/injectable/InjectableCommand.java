package com.github.archemedes.tythan.bukkit.command.injectable;

import com.github.archemedes.tythan.command.TythanCommandTemplate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class InjectableCommand extends TythanCommandTemplate {
    @Getter @NotNull private final String commandName;
    @Getter @Nullable private final String permission;
    @Getter @Nullable private final String description;
    @Getter @NotNull private final String[] commandAlias;
    @Getter @NotNull private final Plugin plugin;

    public InjectableTythanCommandData toInjectableData() {
        List<String> alias = new ArrayList<>();
        if (commandAlias!=null) alias = new ArrayList<>(Arrays.stream(this.commandAlias).toList());
        return new InjectableTythanCommandData(this.plugin,this.commandName,this.permission,this.description,alias);
    }

    public TythanCommandTemplate toTemplate() {
        return this;
    }

}
