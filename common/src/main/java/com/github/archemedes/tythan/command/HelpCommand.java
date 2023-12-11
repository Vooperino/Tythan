package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.CommonKyoriComponentBuilder;
import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import com.github.archemedes.tythan.utils.TColor;

import com.google.common.primitives.Ints;
import lombok.val;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

import java.util.List;


public class HelpCommand extends TythanCommand {
    private static final TColor[] colors = new TColor[] {TColor.AQUA, TColor.DANDELION, TColor.GREEN, TColor.LILAC, TColor.GOLD};
    private final TythanCommand parent;

    HelpCommand(TythanCommand ac) {
        super("help",
                Collections.emptySet(),
                "prints help",
                ac.getPermission(),
                false, true,
                ac.getSenderType(),
                Arrays.asList(helpPageArg()),
                Collections.emptyList(),
                Collections.emptyList(),
                ac.getKyori_based_help_message(),
                ac.getHelpRunCommand(),
                null);
        parent = ac;

    }

    private static @NotNull CommandArgument<Integer> helpPageArg(){
        val c = new CommandArgument<Integer>("page", "not a valid integer", "0", null);
        c.setFilter(i->i>=0);
        c.setMapper(Ints::tryParse);
        return c;
    }

    @Override
    void execute(RanCommand c) {
        int page = c.getArg(0);
        runHelp(c, page);
    }

    public void runHelp(RanCommand c, int page) {

        if(page > 0) {
            int min = 7 + (page-1)*8;
            if(parent.hasDescription()) min--;
            int max = min + 8;
            outputSubcommands(c, min, max);
        }
        else outputBaseHelp(c);
    }

    private void outputBaseHelp(RanCommand c) {
        Sender s = c.getSender();
        var baseHelpComponent = new CommonKyoriComponentBuilder();
        var permissionSubComponent = new CommonKyoriComponentBuilder();
        var flagSubComponent = new CommonKyoriComponentBuilder();
        if(parent.getPermission() != null) baseHelpComponent
                .append("[P]")
                .color(TColor.DANDELION)
                .hoverText(permissionSubComponent
                        .append("Permission required: ", TColor.DANDELION)
                        .append(parent.getPermission(), TColor.GREEN).build());

        if(!parent.getFlags().isEmpty()) {
            baseHelpComponent.append("[F]", TColor.LILAC);
            flagSubComponent
                    .append("Accepted Command Flags:", TColor.DANDELION);
            for (CommandFlag flag : parent.getFlags()) {
                flagSubComponent.newline();
                flagSubComponent.append("-"+flag.getName(), TColor.WHITE);
                String flagPex = flag.getPermission();
                String flagDesc = flag.getArg().getDescription();

                if(flagDesc != null) flagSubComponent.append(": ").append(flagDesc, TColor.GRAY);
                if(flagPex != null) flagSubComponent.append(' ').append("("+flagPex+")", TColor.DANDELION);
            }
            baseHelpComponent.hoverText(flagSubComponent.build());
        }

        commandHeadline(baseHelpComponent,c).send(s);
        var descriptionComponent = new CommonKyoriComponentBuilder().append(parent.getDescription(), TColor.GRAY, TextDecoration.ITALIC).build();
        if(parent.hasDescription()) c.msg(descriptionComponent);

        int max = parent.hasDescription()? 6:7;
        outputSubcommands(c, 0, max);
    }

    private AbstractKyoriComponentBuilder<?> commandHeadline(AbstractKyoriComponentBuilder<?> b, RanCommand c) {
        String alias = "/" + c.getUsedAlias();
        if(alias.endsWith("help")) alias = alias.substring(0, alias.length() - 5);
        b.append(alias, TColor.GOLD);
        b.onClickSuggestCommand(alias);
        fillArgs(parent, alias, b, true);
        return b;
    }

    private void fillArgs(TythanCommand command, String alias, AbstractKyoriComponentBuilder<?> b, boolean useColor) {
        int i = 0;
        for(CommandArgument<?> a : command.getArgs()) {
            boolean optional = a.hasDefaultInput();
            b.append(" ");
            var color = TColor.WHITE;
            if(useColor) color = colorCoded(i++);
            b.append(optional? "[":"<", color);
            if(a.hasDescription()) b.hoverText(a.getDescription());
            else b.onClickSuggestCommand(alias);
            b.append(a.getName(), color)
                    .append(optional? "]":">", color);
        }
    }

    private TColor colorCoded(int i) {
        return colors[i%colors.length];
    }

    void outputSubcommands(@NotNull RanCommand c, int min, int max) {
        Sender s = c.getSender();
        List<TythanCommand> subs = parent.getSubCommands().stream()
                .filter(sub->sub!=this)
                .filter(sub->sub.hasPermission(s))
                .sorted((s1,s2)-> s1.getMainCommand().compareTo(s2.getMainCommand()))
                .toList();

        String alias = "/" + c.getUsedAlias();
        if(alias.endsWith("help")) alias = alias.substring(0, alias.length() - 5);

        if(subs.size() <= min) {
            if(min > 0) s.sendMessage(PrefixStyles.ERROR_PREFIX.append(Component.text("Invalid help page!", TColor.RED)));
            return;
        } else {
            String trailing = alias.substring(alias.lastIndexOf(" ")+1);
            var b = new CommonKyoriComponentBuilder().append("-== Possible sub-commands for ", TColor.NAVY_BLUE)
                    .append(trailing, TColor.GRAY).append(" ==-", TColor.NAVY_BLUE);
            if(min > 0){
                var hoverText = new CommonKyoriComponentBuilder().append("Previous Page", TColor.RED).build();
                b.append(" ").appendBracketed('\u2190', TColor.DARK_GRAY, TColor.RED).hoverText(hoverText).onClickRunCommand(alias + " -h " + (min/8));
            }
            if(subs.size() > max){
                var hoverText = new CommonKyoriComponentBuilder().append("Next Page", TColor.RED).build();
                b.append(" ").appendBracketed('\u2192', TColor.DARK_GRAY, TColor.RED).hoverText(hoverText).onClickRunCommand(alias + " -h " + ((min+2)/8+1));
            }

            c.msg(b.build());
        }

        //Arrays start at 1 fight me
        for(int i = min; i < max; i++) {
            if(subs.size() <= i) break;
            TythanCommand sub = subs.get(i);
            String subber = sub.getMainCommand();

            var b = new CommonKyoriComponentBuilder().append(subber, TColor.DANDELION);
            if(sub.getHelp() != null) b.onClickRunCommand(alias + ' ' + subber + " -h 0").hoverText("Click for help on this subcommand!");
            else b.onClickSuggestCommand(alias + ' ' + subber + ' ').hoverText("Click to run this command");
            fillArgs(sub, alias + ' ' + subber, b, false);

            if(sub.hasDescription()) {
                int room = 57 - PlainComponentSerializer.plain().serialize(b.build()).length();
                if(room > 0)  {
                    b.append(": ");
                    String desc = sub.getDescription();
                    if(desc.length() > room) desc = desc.substring(0, room-1) + '\u2026';
                    b.append(desc, TColor.GRAY);
                }
            }

            b.send(s);
        }

    }

}
