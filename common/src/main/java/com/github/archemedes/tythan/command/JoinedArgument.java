package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinedArgument extends CommandArgument<String> {


    public JoinedArgument(String name, String errorMessage, String defaultInput, String description) {
        super(name, defaultInput, description, errorMessage);
        this.setMapper(s->s);
        this.setBrigadierType(StringArgumentType.greedyString());
    }

    @Override String resolve(Sender s, @NotNull List<String> input, int i) {
        List<String> relevantInput = input.subList(i, input.size());
        String joined = String.join(" ", relevantInput);
        return resolve(s, joined);
    }
}
