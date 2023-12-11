package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.utils.TColor;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AgnosticExecutor {
    private final TythanCommand rootCommand;
    public boolean onCommand(Sender sender, String label, String[] args) {
        List<String> listArgs = new ArrayList<>();
        for (String arg : args) listArgs.add(arg);
        runCommand(sender, rootCommand, label, listArgs);
        return true;
    }
    public List<String> onTabComplete(Sender sender, String alias, String[] args) {
        if (!this.rootCommand.hasPermission(sender)) return new ArrayList<>();
        if (this.rootCommand.isHiddenCommand()) return new ArrayList<>();
        try{
            List<String> listArgs = new ArrayList<>();
            for (String arg : args) listArgs.add(arg);
            return getCompletions(sender, rootCommand, listArgs);
        } catch(ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }
    private List<String> getCompletions(Sender sender, TythanCommand command, List<String> args) {
        TythanCommand subCommand = wantsSubCommand(command,sender,args);
        if(subCommand != null) {
            if (!subCommand.hasPermission(sender)) return new ArrayList<>();
            if (subCommand.isHiddenCommand()) return new ArrayList<>();
            args.remove(0);
            return getCompletions(sender, subCommand, args);
        } else {
            if (command.isHiddenCommand()) return new ArrayList<>();
            if (!command.hasPermission(sender)) return new ArrayList<>();
            List<String> options;
            if(args.isEmpty()) return Lists.newArrayList();
            int index = args.size() - 1;
            String last = args.get(index).toLowerCase();
            if(args.size() == 1) options = subCompletions(sender, command, last);
            else options = new ArrayList<>();
            if(index < command.getArgs().size())
                command.getArgs()
                        .get(index)
                        .getCompleter()
                        .suggest(sender, args.get(index))
                        .forEach(sugg->options.add(sugg.getLiteral()));
            return options.stream().filter(Objects::nonNull).filter(s->s.toLowerCase().startsWith(last)).collect(Collectors.toList());
        }
    }

    private void runCommand(Sender sender, TythanCommand command, String usedAlias, List<String> args) {
        TythanCommand subCommand = wantsSubCommand(command,sender,args);
        TythanInstanceProvider.debug(Level.WARNING,"Catching Alias " + usedAlias + ". SubCommand found: " + subCommand);
        if(!args.isEmpty()) TythanInstanceProvider.debug(Level.WARNING,"These are its arguments: " + StringUtils.join(args, ", "));
        if(subCommand != null) {
            runSubCommand(sender, subCommand, usedAlias, args);
        } else if (!command.hasPermission(sender)) {
            sender.sendMessage(PrefixStyles.ERROR_PREFIX.append(Component.text("You do not have permission to use this", TColor.WHITE)));
        } else {
            RanCommand c = new RanCommand(command, usedAlias, sender);
            try{
                c.parseAll(args);
            } catch(Exception e) {
                c.handleException(e);
                return;
            }
            HelpCommand help = command.getHelp();
            if(help != null && c.hasFlag("h")) {
                help.runHelp(c, c.getFlag("h"));
            } else {
                executeCommand(command, c);
            }
        }
    }

    private void runSubCommand(Sender sender, TythanCommand subCommand, String usedAlias, List<String> args) {
        if(subCommand.isInvokeOverload()) {
            runCommand(sender, subCommand, usedAlias, args);
        } else {
            String usedSubcommandAlias = args.remove(0).toLowerCase();
            String newAlias = usedAlias + ' ' + usedSubcommandAlias;
            runCommand(sender, subCommand, newAlias, args);
        }
    }

    private void executeCommand(TythanCommand command, RanCommand c) {
        try {
            command.execute(c);
        } catch(RanCommand.CmdParserException e) {
            c.handleException(e);
        }
    }

    private List<String> subCompletions(Sender sender, TythanCommand cmd, String argZero){
        List<String> result = new ArrayList<>();
        String lower = argZero.toLowerCase();
        cmd.getSubCommands().stream()
                .filter(Objects::nonNull)
                .filter(s->s.hasPermission(sender))
                .filter(s->!s.isHiddenCommand())
                .map(s->s.getBestAlias(lower))
                .forEach(result::add);
        return result;
    }

    private TythanCommand wantsSubCommand(TythanCommand cmd, Sender sender, List<String> args) {
        List<TythanCommand> matches = new ArrayList<>();
        cmd.getSubCommands().stream()
                .filter(TythanCommand::isInvokeOverload)
                .filter(ac->ac.fitsArgSize(args.size()))
                .filter(ac->ac.hasPermission(sender))
                .forEach(matches::add);
        if(matches.size() > 1) throw new IllegalStateException("Invoke overload ambiguity: Same arg size took multiple args!!");
        else if(matches.size() == 1) return matches.get(0);
        if(args.isEmpty()) return null;
        String subArg = args.get(0).toLowerCase();
        cmd.getSubCommands().stream()
                .filter(s->!s.getMainCommand().isEmpty())
                .filter(s->s.isAlias(subArg))
                .filter(s->s.hasPermission(sender))
                .forEach(matches::add);
        if(matches.isEmpty()) return null;
        else if(matches.size() == 1) return matches.get(0);
        int s = args.size() - 1;
        for(TythanCommand match : matches) if(match.fitsArgSize(s)) return match;
        return null;
    }

}
