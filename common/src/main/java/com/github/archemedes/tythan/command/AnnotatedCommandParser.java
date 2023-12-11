package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.agnostic.Command;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.command.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class AnnotatedCommandParser {
    private final Supplier<TythanCommandTemplate> template;
    private final Command pluginCommand;

    public TythanCommandBuilder invokeParse(Consumer<TythanCommand> registrationHandler) {
        TythanCommandBuilder acb = new TythanCommandBuilder(registrationHandler, pluginCommand);
        return parse(template, acb);
    }

    private TythanCommandBuilder parse(Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb) {
        Class<? extends TythanCommandTemplate> c = template.get().getClass();
        addInvoke(c, template, acb);
        var cmds = Stream.of(c.getMethods()).filter(m->m.isAnnotationPresent(com.github.archemedes.tythan.command.annotations.Command.class)).toList();
        for(Method method : cmds) checkForSubLayer(method, template, acb);
        for(Method method : cmds) parseCommand(method, template, acb);
        return acb;
    }

    private void addInvoke(Class<? extends TythanCommandTemplate> c, Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb) {
        boolean firstInvokeFound = false;
        for(Method m : c.getDeclaredMethods()) {
            if(m.getParameterCount() > 0 && Modifier.isPublic(m.getModifiers()) && m.getName().equals("invoke") && m.getReturnType() == Void.TYPE) {
                if(!firstInvokeFound) {
                    parseCommandMethod(m, template, acb);
                    firstInvokeFound = true;
                } else {
                    var overload = acb.overloadInvoke();
                    parseCommandMethod(m, template, overload);
                    overload.build();
                }
            }
        }
        if(!firstInvokeFound) acb.payload(rc->{
            TythanCommandTemplate t = template.get();
            t.setRanCommand(rc);
            t.invoke();
        });
    }

    private TythanCommandBuilder constructSubBuilder(Method method, TythanCommandBuilder parent) {
        String name = method.getName();
        com.github.archemedes.tythan.command.annotations.Command anno = method.getAnnotation(com.github.archemedes.tythan.command.annotations.Command.class);
        HelpMessage helpMessage = method.getAnnotation(HelpMessage.class);
        SubCommandRename subCommandRename = method.getAnnotation(SubCommandRename.class);

        boolean hiddenCommand = method.getAnnotation(HiddenCommand.class)!=null;

        if(!anno.alias().isEmpty()) name = anno.alias();
        String desc = anno.value();
        String pex = anno.permission();
        @Nullable HelpMessage help = null;
        @Nullable SubCommandRename rename = null;
        if (helpMessage!=null) help = helpMessage;
        if (subCommandRename!=null) {
            String newCommandName = subCommandRename.newName();
            rename = subCommandRename;
            name = newCommandName.replaceAll(" ","");
            if (name.contains(" ")) throw new IllegalStateException("Spaces are not allowed in the @SubCommandRename");
        }
        boolean flags = anno.flags();
        var result = parent.subCommand(name,help,rename,false,anno.dontShowInHelp());
        result.alias(anno.aliases());
        result.hiddenCommand(hiddenCommand);
        result.dontShowInHelp(anno.dontShowInHelp());
        if(!flags) result.noFlags();
        if(desc !=  null) result.description(desc);
        if(StringUtils.isNotEmpty(pex)) result.permission(pex);

        return result;
    }

    @SneakyThrows
    private void checkForSubLayer(Method method, Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb) {
        if(!TythanCommandTemplate.class.isAssignableFrom(method.getReturnType())) return;
        if(method.getParameterCount() > 0) throw new IllegalStateException("Methods returning TythanCommandTemplate can't also have parameters");
        if(method.getName().equals("invoke")) {
            if (method.isAnnotationPresent(SubCommandRename.class)) throw new IllegalStateException("SubCommandRename annotation is not allowed for invoke() method!");
            if (method.isAnnotationPresent(OnHelpRunCommand.class)) throw new IllegalStateException("OnHelpRunCommand annotation is not allowed for invoke() method!");
            if (method.isAnnotationPresent(HiddenCommand.class)) throw new IllegalStateException("HiddenCommand annotation is not allowed for invoke() method!");
            throw new IllegalArgumentException("Don't annotate your invoke() methods. The method name is reserved!");
        }

        TythanCommandBuilder subbo = constructSubBuilder(method, acb);
        Supplier<TythanCommandTemplate> chained = ()-> chainSupplier(method, template);
        parse(chained, subbo).build();
    }

    @SneakyThrows
    private TythanCommandTemplate chainSupplier(Method templateGetter, Supplier<TythanCommandTemplate> theOldSupplier) {
        return (TythanCommandTemplate) templateGetter.invoke(theOldSupplier.get());
    }

    private void parseCommand(Method method, Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb) {
        if(method.getReturnType() != Void.TYPE) return;
        if(method.getName().equals("invoke")) {
            if (method.isAnnotationPresent(SubCommandRename.class)) throw new IllegalStateException("SubCommandRename annotation is not allowed for invoke() method!");
            if (method.isAnnotationPresent(OnHelpRunCommand.class)) throw new IllegalStateException("OnHelpRunCommand annotation is not allowed for invoke() method!");
            if (method.isAnnotationPresent(HiddenCommand.class)) throw new IllegalStateException("HiddenCommand annotation is not allowed for invoke() method!");
            throw new IllegalArgumentException("Don't annotate your invoke() methods. The method name is reserved!");
        }

        var subbo = constructSubBuilder(method, acb);
        parseCommandMethod(method, template, subbo);
        subbo.build();
    }

    private void parseCommandMethod(Method method, Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb) {
        HelpMessage helpMessage = method.getAnnotation(HelpMessage.class);
        OnHelpRunCommand helpRunCommand = method.getAnnotation(OnHelpRunCommand.class);
        if (helpMessage!=null) acb.help_message(helpMessage);
        if (helpRunCommand!=null) acb.helpRunCommand(helpRunCommand);
        acb.hiddenCommand(method.getAnnotation(HiddenCommand.class)!=null);
        var flagsAnno = method.getAnnotation(Flag.List.class);
        if(flagsAnno != null) for(Flag flag : flagsAnno.value()) addFlag(acb, flag);
        else if(method.isAnnotationPresent(Flag.class)) addFlag(acb, method.getAnnotation(Flag.class));
        var params = method.getParameters();
        boolean wantsSenderAsFirstArg = false;
        for (int i = 0; i < params.length; i++) {
            var param = params[i];
            var c = param.getType();
            if (acb.hiddenCommand()) TythanInstanceProvider.debug(Level.WARNING,c+":"+method.getName()+" has a the @HiddenCommand annotation attached to it! This could lead to some potential issues.");
            TythanInstanceProvider.debug(Level.INFO,"Param " + i + " in method " + method.getName() + " has type " + c);
            if(i == 0) {
                if(ParameterType.senderTypeExists(c)) {
                    TythanInstanceProvider.debug(Level.INFO,"Method " + method.getName() + " for cmd " + acb.mainCommand() +
                            " wants sender type: " + ParameterType.getCustomType(c).getTargetType());
                    acb.requiresSender(c);
                    continue;
                } else if( Sender.class.isAssignableFrom(c)) {
                    wantsSenderAsFirstArg = true;
                    continue;
                }
            }
            var argAnno = param.getAnnotation(Argument.class);
            ArgumentBuilder arg = argAnno == null? acb.arg() : acb.arg(argAnno.value());
            if(argAnno != null && !argAnno.description().isEmpty()) arg.description(argAnno.description());
            Default defaultInput = param.getAnnotation(Default.class);
            if(defaultInput != null) {
                String def = defaultInput.value();
                Validate.notNull(def);
                arg.defaultInput(def);
            }
            if(param.isAnnotationPresent(Joined.class)) {
                if(param.getType() == String.class) arg.asJoinedString();
                else throw new IllegalArgumentException("All JoinedString annotations must affect a String type parameter");
            } else if (param.isAnnotationPresent(Range.class)) {
                Range rangeInput = param.getAnnotation(Range.class);
                boolean hasMin = rangeInput.min() != Integer.MIN_VALUE;
                boolean hasMax = rangeInput.max() != Integer.MAX_VALUE;
                if(c == int.class || c == Integer.class) {
                    if(hasMin && hasMax) arg.asInt((int)rangeInput.min(), (int)rangeInput.max());
                    else if(hasMin && !hasMax) arg.asInt((int) rangeInput.min());
                    else throw new IllegalArgumentException("Use @Range by specifying either a min or a min and max");
                } else if(c == float.class || c == Float.class) {
                    if(hasMin && hasMax) arg.asFloat((float)rangeInput.min(), (float) rangeInput.max());
                    else if(hasMin && !hasMax) arg.asFloat((float) rangeInput.min());
                    else throw new IllegalArgumentException("Use @Range by specifying either a min or a min and max");
                } else if(c == double.class || c == Double.class) {
                    if(hasMin && hasMax) arg.asDouble(rangeInput.min(), rangeInput.max());
                    else if(hasMin && !hasMax) arg.asDouble( rangeInput.min());
                    else throw new IllegalArgumentException("Use @Range by specifying either a min or a min and max");
                } else {
                    throw new IllegalArgumentException("Use @Range annotation only on integer, float or double!");
                }
            } else {
                arg.asType(c);
            }
        }
        makeCommandDoStuff(template, acb, method, wantsSenderAsFirstArg);
    }

    private void makeCommandDoStuff(Supplier<TythanCommandTemplate> template, TythanCommandBuilder acb, Method method, boolean wantsCommandSenderAsFirstArg) {
        //Make command actually do stuff
        acb.payload(rc->{
            try {
                TythanCommandTemplate t = template.get();
                t.setRanCommand(rc);
                Object[] args = rc.getArgResults().toArray();

                if(acb.requiresSender()) {
                    Object[] newArgs = insertFirst(args, rc.getResolvedSender());
                    method.invoke(t, newArgs);
                } else if (wantsCommandSenderAsFirstArg) {
                    Object[] newArgs = insertFirst(args, rc.getSender());
                    method.invoke(t, newArgs);
                }else {
                    method.invoke(t, args);
                }
            } catch (InvocationTargetException ite) {
                if(ite.getCause() instanceof RanCommand.CmdParserException) {
                    rc.error(Component.text(ite.getCause().getMessage()));
                } else {
                    ite.printStackTrace();
                    rc.error(Component.text().content("An error occurred while attempting to perform this command!").asComponent());
                }
            } catch (Exception e) {
                e.printStackTrace();
                rc.error(Component.text().content("An error occurred while attempting to perform this command!").asComponent());
            }
        });
    }


    private Object[] insertFirst(Object[] args, Object toAdd) {
        Object[] newArgs = new Object[args.length+1];
        System.arraycopy(args, 0, newArgs, 1, args.length);
        newArgs[0] = toAdd;
        return newArgs;
    }

    private void addFlag(TythanCommandBuilder acb, Flag flag) {
        ArgumentBuilder flarg;
        String pex = flag.permission();
        boolean tab = flag.showTabComplete();
        if(!pex.isEmpty()) flarg = acb.restrictedFlag(flag.name(), pex,tab, flag.aliases());
        else flarg = acb.flag(flag.name(),tab, flag.aliases());
        String desc = flag.description();
        if(!desc.isEmpty()) flarg.description(desc);
        flarg.asType(flag.type());
    }

}
