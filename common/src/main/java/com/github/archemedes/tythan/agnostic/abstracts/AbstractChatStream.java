package com.github.archemedes.tythan.agnostic.abstracts;

import com.github.archemedes.tythan.agnostic.CommonKyoriComponentBuilder;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.utils.TColor;
import com.github.archemedes.tythan.utils.collections.Context;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


@RequiredArgsConstructor
@SuppressWarnings("unused")
public abstract class AbstractChatStream<T extends AbstractChatStream<T>> {
    @NotNull protected final Sender converser;
    @NotNull
    protected final UUID uuid;
    @NotNull protected final Context context = new Context();
    @NotNull protected final List<Prompt> prompts = new ArrayList<>();
    protected Consumer<Context> onAbandon, onActivate;
    @Nullable Component abandonButton,abandonMessage,timeoutMessage;
    boolean includeAbandonButton = true;

    public T timeoutMessage(Component button){
        this.timeoutMessage = button;
        return getThis();
    }

    public T abandonMessage(Component message){
        this.abandonMessage = message;
        return getThis();
    }

    public T abandonButton(Component button){
        this.abandonButton = button;
        return getThis();
    }

    public T deludeAbandonButton(){
        this.includeAbandonButton = false;
        return getThis();
    }

    public T prompt(Prompt prompt) {
        prompts.add(prompt);
        return getThis();
    }

    public T prompt(String contextTag, Component promptText, Consumer<Prompt> fulfillment, boolean includeAbandonButton,
                    @Nullable Component abandonButton, @Nullable Component abandonMessage, @Nullable Component timeoutMessage) {
        Prompt p = new Prompt(this, contextTag, promptText,abandonButton,abandonMessage,timeoutMessage,includeAbandonButton,fulfillment);
        return prompt(p);
    }

    public T confirmPrompt() {
        var msg = new CommonKyoriComponentBuilder().append("Are you sure you want to continue? Type ", TColor.WHITE)
                .append("YES", TColor.RED).bold().append(" in all capitals to confirm your choice.", TColor.WHITE).build();
        return confirmPrompt(msg, "YES");
    }

    public T confirmPrompt(String whatTheyShouldType) {
        var msg = new CommonKyoriComponentBuilder().append("Are you sure you want to continue? Type ", TColor.WHITE)
                .append(whatTheyShouldType, TColor.RED).bold().append(" to confirm your choice (case-sensitive).", TColor.WHITE).build();
        return confirmPrompt(msg, "YES");
    }

    public T confirmPrompt(Component message, String whatTheyShouldType) {
        return prompt(null, message, s->s.equals(whatTheyShouldType), $->$);
    }


    public T choice(String contextTag, Component message, String... options) {
        var cb = new CommonKyoriComponentBuilder().append(message).newline();
        for(String option : options) cb.appendButton(option, option);
        message = cb.build();

        Function<String, String> maps = s-> (Stream.of(options).filter(o->s.equalsIgnoreCase(s)).findAny().orElse(null));
        return prompt(contextTag, message, maps);
    }

    public T prompt(String contextTag, String message) {
        return prompt(contextTag, Component.text(message));
    }

    public T prompt(String contextTag, Component message) {
        Predicate<String> somePredicate = $->true; //Compiler wants it explicit in generic type
        return prompt( contextTag, message, somePredicate );
    }

    public T prompt(String contextTag, String message, Predicate<String> filter) {
        return prompt(contextTag, Component.text(message), filter);
    }

    public T prompt(String contextTag, Component message, Predicate<String> filter) {
        return prompt(contextTag, message, filter, $->$);
    }

    public T prompt(String contextTag, Component message, Function<String, ?> mapper) {
        return prompt(contextTag, message, $->true, $->$);
    }

    public T prompt(String contextTag, String message, Predicate<String> filter, Function<String, ?> mapper) {
        return prompt(contextTag, Component.text(message), filter, mapper);
    }

    public abstract T prompt(String contextTag, Component message, Predicate<String> filter, Function<String, ?> mapper);


    public T intPrompt(String contextTag, String message) {
        return intPrompt(contextTag, Component.text(message));
    }

    public T intPrompt(String contextTag, Component message) {
        return prompt(contextTag, message, NumberUtils::isDigits, Ints::tryParse);
    }

    public T doublePrompt(String contextTag, String message) {
        return doublePrompt(contextTag, Component.text(message));
    }

    //Would optimally like to use isParsable from NumberUtils, however we aren't using lang3. If we move to lang3, change this.
    public T doublePrompt(String contextTag, Component message) {
        return prompt(contextTag, message, NumberUtils::isNumber, Doubles::tryParse);
    }

    public T withContext(String key, Object value) {
        context.set(key, value);
        return getThis();
    }

    public T withContext(Context context) {
        context.getMap().forEach(this.context::set);
        return getThis();
    }

    public void activate(Consumer<Context> go) {
        onActivate = go;
        Validate.isTrue(!prompts.isEmpty());
        prompts.get(0).open();
    }

    public T withAbandonment(Consumer<Context> stop) {
        onAbandon = stop;
        return getThis();
    }

    protected abstract void resolveFinishedStream();

    protected abstract void resolveAbaondonedStream();

    protected abstract T getThis();


    @RequiredArgsConstructor
    @FieldDefaults(level= AccessLevel.PRIVATE, makeFinal=true)
    @Getter
    public static final class Prompt {

        AbstractChatStream<?> stream;
        String contextTag;
        Component text,abandonButton,abandonMessage,timeoutMessage;
        boolean includeAbandonButton;
        Consumer<Prompt> fulfillment;

        public Sender getConverser() {
            return stream.converser;
        }

        void open() {
            sendPrompt();
            fulfillment.accept(this);
        }

        public void sendPrompt() {
            var a = new CommonKyoriComponentBuilder();
            if (includeAbandonButton) a.appendWithSpace(abandonButton);
            a.append(text).send(stream.converser);
        }

        public void fulfil(Object value) {
            if(StringUtils.isNotEmpty(contextTag)) stream.context.set(contextTag, value);
            next();
        }

        void next() {
            int i = stream.prompts.indexOf(this) + 1;
            if(i >= stream.prompts.size()) stream.resolveFinishedStream();
            else stream.prompts.get(i).open();
        }

        public void abandon() {
            stream.resolveAbaondonedStream();
        }
    }
}
