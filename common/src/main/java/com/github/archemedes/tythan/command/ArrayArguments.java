package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ArrayArguments extends CommandArgument<String[]> {

    public ArrayArguments(String name, String errorMessage, String defaultInput, String description) {
        super(name, defaultInput, description, errorMessage);
        this.setMapper(s->s.split(" "));
        this.setBrigadierType(StringArgumentType.greedyString());
    }

    @Override String[] resolve(Sender s, List<String> input, int i) {
        List<String> relevantInput = input.subList(i, input.size());
        String joined = String.join(" ", relevantInput);
        return resolve(s, joined);
    }

}
