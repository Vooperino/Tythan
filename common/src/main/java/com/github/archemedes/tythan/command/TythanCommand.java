package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.command.annotations.HelpMessage;
import com.github.archemedes.tythan.command.annotations.OnHelpRunCommand;
import lombok.*;
import lombok.experimental.NonFinal;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

@Value @NonFinal @RequiredArgsConstructor
public class TythanCommand {
    public static final Consumer<RanCommand> NULL_COMMAND = rc->{};

    String mainCommand;
    Set<String> aliases;
    String description;
    String permission;

    boolean dontShowInHelp,hiddenCommand;

    ParameterType<?> senderType;

    List<CommandArgument<?>> args;
    List<CommandFlag> flags;
    List<TythanCommand> subCommands;

    @Nullable HelpMessage kyori_based_help_message;
    @Nullable OnHelpRunCommand helpRunCommand;

    @Getter(AccessLevel.NONE) Consumer<RanCommand> payload;

    void execute(RanCommand rc) {
        payload.accept(rc);
    }

    public boolean isEmptyCommand() {
        return payload == NULL_COMMAND;
    }

    public boolean hasArgs() {
        return !args.isEmpty();
    }

    public boolean hasPermission(Sender s) {
        return StringUtils.isEmpty(permission) || s.hasPermission(permission);
    }

    public boolean hasDescription() {
        return StringUtils.isNotEmpty(description);
    }

    public boolean isAlias(String param) {
        return aliases.contains(param);
    }

    public String getBestAlias(String param) {
        for(String alias : aliases) if(alias.startsWith(param)) return alias;
        return null;
    }

    private boolean aliasOverlaps(@NotNull TythanCommand other) {
        return aliases.stream().anyMatch(other::isAlias);
    }

    boolean argRangeOverlaps(@NotNull TythanCommand other) { //b1 <= a2 && a1 <= b2
        return other.minArgs() <= maxArgs() && minArgs() <= other.maxArgs();
    }

    boolean collides(@NotNull List<TythanCommand> subbos) {
        return subbos.stream()
                .filter(this::argRangeOverlaps)
                .anyMatch(this::aliasOverlaps);
    }

    HelpCommand getHelp() {
        return subCommands.stream()
                .filter(HelpCommand.class::isInstance)
                .map(HelpCommand.class::cast)
                .findAny().orElse(null);
    }

    private int minArgs() {
        int i = 0;
        for(val arg : args) {
            if(arg.hasDefaultInput()) return i;
            i++;
        }

        return i;
    }

    private int maxArgs() {
        int s = args.size();
        if(s > 0 && args.get(s-1) instanceof JoinedArgument) return 255;
        else if(s > 0 && args.get(s-1) instanceof ArrayArguments) return 255;
        else return s;
    }

    public boolean isInvokeOverload() {
        return mainCommand.isEmpty();
    }

    public boolean fitsArgSize(int argSize) {
        return argSize >= minArgs() && argSize <= maxArgs();
    }

    @Override
    public String toString() {
        return "TythanCommand:" + mainCommand;
    }

}
