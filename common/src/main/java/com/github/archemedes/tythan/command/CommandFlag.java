package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.Sender;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;

import java.util.*;

@FieldDefaults(level=AccessLevel.PRIVATE) @Getter
public class CommandFlag {
    final Set<String> aliases;
    final String permission;
    @Setter CommandArgument<?> arg;
    @Getter final boolean showTab;

    private CommandFlag(String name, String pex, boolean showTab, String... flagAliases){
        this.permission = pex;
        this.showTab = showTab;
        Set<String> als = new HashSet<>();
        als.add(name);
        als.addAll(Arrays.asList(flagAliases));
        aliases = Collections.unmodifiableSet(als);
    }

    public static ArgumentBuilder make(TythanCommandBuilder target, String name, boolean showTab,String... flagAliases) {
        return make(target,name,null,showTab,flagAliases);
    }

    public static ArgumentBuilder make(TythanCommandBuilder target, String name, String pex, boolean showTab,String... flagAliases) {
        CommandFlag flag = new CommandFlag(name,pex,showTab,flagAliases);
        if(flag.collidesWithAny(target.flags())) throw new IllegalStateException("Flag aliases are overlapping for command: " + target.mainCommand());
        target.addFlag(flag);
        ArgumentBuilder builder = new ArgumentBuilder(target, flag).name(name);
        return builder;
    }

    public String getName() {
        return arg.getName();
    }

    public boolean needsPermission() {
        return this.permission != null;
    }

    public boolean hasPermission(Sender s) {
        return StringUtils.isEmpty(permission) || s.hasPermission(permission);
    }

    public boolean collidesWithAny(List<CommandFlag> flags) {
        return flags.stream().anyMatch(this::collidesWith);
    }

    public boolean collidesWith(CommandFlag flag) {
        for(String alias : aliases) if(flag.getAliases().stream().anyMatch(alias::equals)) return true;
        return false;
    }

    boolean mayUse(Sender s) {
        return StringUtils.isEmpty(permission) || s.hasPermission(permission);
    }

    public boolean isVoid() {
        return this.arg instanceof VoidArgument;
    }


}
