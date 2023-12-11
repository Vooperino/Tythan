package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import net.kyori.adventure.text.Component;

public interface CommandHandle {
    void msg(String message, Object... format);
    void msg(Object message);
    void msgRaw(String message);
    Sender getSender();
    void msgRich(String mini_message);
    void msg(Component message);
    void msg(AbstractKyoriComponentBuilder abstractComponentBuilder);
    void error(AbstractKyoriComponentBuilder err);
    void error(Component err);
    void error(String err);

    void errorWithRichText(String mini_message);
    void validate(boolean condition, AbstractKyoriComponentBuilder error);
    void validate(boolean condition, Component error);
    void validate(boolean condition, String error);
    void validateWithRichText(boolean condition, String mini_msg_error);
    boolean hasFlag(String flagName);
    <T> T getFlag(String flagName);

}
