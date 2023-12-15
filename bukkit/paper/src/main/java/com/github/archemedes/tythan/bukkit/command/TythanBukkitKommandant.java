package com.github.archemedes.tythan.bukkit.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitSender;
import com.github.archemedes.tythan.command.CommandArgument;
import com.github.archemedes.tythan.command.CommandFlag;
import com.github.archemedes.tythan.command.TythanCommand;
import com.github.archemedes.tythan.command.brigadier.Kommandant;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.List;

public class TythanBukkitKommandant extends Kommandant {
    public TythanBukkitKommandant(TythanCommand head) {
        super(head);
    }

    @Override
    protected RequiredArgumentBuilder makeBuilderWithSuggests(String name, ArgumentType<?> type, CommandArgument<?> arg, TythanCommand cmd) {
        var builder = RequiredArgumentBuilder.argument(name, type);
        SuggestionProvider provider = new TythanCommandSuggestionProvider(arg,cmd);
        builder.suggests(provider);
        return builder;

    }

    @Override
    protected boolean hasPlayerPermission(CommandArgument<?> arg) {
        return false;
    }

    @Override
    protected <T> SuggestionProvider<T> getFlagSuggestionProvider(List<CommandFlag> flags, TythanCommand cmd) {
        return (context, builder) -> {
            T source = context.getSource();
            Sender sender = new BukkitSender(BrigadierProvider.get().getBukkitSender(source));
            for (CommandFlag flag : flags) {
                if (!cmd.hasPermission(sender)) continue;
                if (cmd.isHiddenCommand()) continue;
                if (!flag.isShowTab()) continue;
                if (!flag.hasPermission(sender))continue;
                builder.suggest("-" + flag.getName());
            }
            return builder.buildFuture();
        };

    }
}
