package com.github.archemedes.tythan.command;

import net.kyori.adventure.text.Component;

public class VoidArgument extends CommandArgument<Void> {
    public VoidArgument(String name, String errorMessage, String description) {
        super(name,  "IF_YOU_SEE_THIS_SOMETHING_WENT_WRONG", description, errorMessage);
        this.setMapper(s->null);
    }
}
