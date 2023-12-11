package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.CommonKyoriComponentBuilder;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.utils.TColor;
import lombok.AccessLevel;
import lombok.Setter;
import net.kyori.adventure.text.Component;

public class TythanCommandTemplate implements CommandHandle {
    @Setter(AccessLevel.PACKAGE) RanCommand ranCommand;
    @Override public Sender getSender() {return ranCommand.getSender();}
    @Override public void msg(String message, Object... format) {ranCommand.msg(message, format);}
    @Override public void msg(Component message) {ranCommand.msg(message);}
    @Override public void msg(Object o) {ranCommand.msg(o);}
    @Override public void msgRich(String mini_message) {ranCommand.msg(new CommonKyoriComponentBuilder().appendRich(mini_message));}
    @Override public void msg(AbstractKyoriComponentBuilder abstractComponentBuilder) {ranCommand.msg(abstractComponentBuilder);}
    @Override public void msgRaw(String message) {ranCommand.msgRaw(message);}
    @Override public void error(AbstractKyoriComponentBuilder err) {ranCommand.error(err);}
    @Override public void error(Component err) {ranCommand.error(err);}
    @Override public void error(String err) {error(Component.text(err, TColor.WHITE));}
    @Override public void errorWithRichText(String mini_message) {error(new CommonKyoriComponentBuilder().appendRich(mini_message));}
    @Override public void validate(boolean condition, String error) {ranCommand.validate(condition, Component.text(error, TColor.WHITE));}
    @Override public void validate(boolean condition, Component error) {ranCommand.validate(condition, error);}
    @Override public void validate(boolean condition, AbstractKyoriComponentBuilder error) {ranCommand.validate(condition, error);}
    @Override public void validateWithRichText(boolean condition, String mini_msg_error) {ranCommand.validate(condition, new CommonKyoriComponentBuilder().appendRich(mini_msg_error));}
    @Override public boolean hasFlag(String flagName) {return ranCommand.hasFlag(flagName);}
    @Override public <T> T getFlag(String flagName) {return ranCommand.getFlag(flagName);}
    protected void invoke() {
        ranCommand.getArgResults().add(0); //Dummy help page argument. Prevents error
        ranCommand.getCommand().getHelp().execute(ranCommand);
    }


}
