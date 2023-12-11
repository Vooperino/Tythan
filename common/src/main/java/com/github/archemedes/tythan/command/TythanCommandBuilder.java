package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.Tythan;
import com.github.archemedes.tythan.TythanInstanceProvider;
import com.github.archemedes.tythan.agnostic.Command;
import com.github.archemedes.tythan.command.annotations.HelpMessage;
import com.github.archemedes.tythan.command.annotations.OnHelpRunCommand;
import com.github.archemedes.tythan.command.annotations.SubCommandRename;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Accessors(fluent=true)
public class TythanCommandBuilder {
    private final Consumer<TythanCommand> registrationHandler;
    private final TythanCommandBuilder parentBuilder;
    private final Command command;
    @Nullable @Getter @Setter private HelpMessage help_message;
    @Nullable @Getter @Setter private OnHelpRunCommand helpRunCommand;

    @Getter private String mainCommand;
    @Setter private String description;
    @Setter private String permission;
    @Setter @Getter private boolean dontShowInHelp,hiddenCommand;

    @Getter private ParameterType<?> senderType = null;

    @Getter(AccessLevel.PACKAGE) private final Set<String> aliases = new HashSet<>();
    @Getter(AccessLevel.PACKAGE) private final List<CommandArgument<?>> args = new ArrayList<>();
    @Getter(AccessLevel.PACKAGE) private final List<CommandFlag> flags = new ArrayList<>();
    @Getter(AccessLevel.PACKAGE) private final List<TythanCommand> subCommands = new ArrayList<>();

    @Setter private Consumer<RanCommand> payload = null;

    //Builder state booleans
    boolean argsHaveDefaults = false; //When arg is added that has default input
    boolean noMoreArgs = false; //When an unity argument is used
    boolean buildHelpFile = true;
    boolean useFlags = true;


    public TythanCommandBuilder(Consumer<TythanCommand> registration, Command command) {
        registrationHandler = registration;
        parentBuilder = null;
        this.command = command;

        this.mainCommand = command.getName();
        this.description = command.getDescription();
        this.permission = command.getPermission();

        command.getAliases().stream().map(String::toLowerCase).forEach(aliases::add);
        aliases.add(command.getName().toLowerCase());
    }
    public TythanCommandBuilder(Consumer<TythanCommand> registration, HelpMessage help, Command command) {
        registrationHandler = registration;
        parentBuilder = null;
        this.command = command;

        this.mainCommand = command.getName();
        this.description = command.getDescription();
        this.permission = command.getPermission();
        this.help_message = help;

        command.getAliases().stream().map(String::toLowerCase).forEach(aliases::add);
        aliases.add(command.getName().toLowerCase());
    }


    TythanCommandBuilder(TythanCommandBuilder dad, String name, boolean inheritOptions){
        registrationHandler = null;
        parentBuilder = dad;
        command = dad.command;
        this.mainCommand = name;
        this.permission = dad.permission;
        this.help_message = dad.help_message;
        this.hiddenCommand = dad.hiddenCommand;
        aliases.add(name.toLowerCase());
        if(inheritOptions) {
            this.buildHelpFile = dad.buildHelpFile;
        }
    }
    TythanCommandBuilder(TythanCommandBuilder dad, String name, HelpMessage help, boolean inheritOptions) {
        this(dad, name, inheritOptions);
        this.help_message = help;
    }
    TythanCommandBuilder(TythanCommandBuilder dad, String name, HelpMessage help, SubCommandRename subCommandRename, boolean inheritOptions) {
        this(dad, name, inheritOptions);
        this.help_message = help;
        if (subCommandRename!=null){
            this.mainCommand = subCommandRename.newName().replaceAll(" ","");
            if (this.mainCommand.contains(" ")) throw new IllegalStateException("Spaces are not allowed in the @SubCommandRename");
        }
    }
    TythanCommandBuilder(TythanCommandBuilder dad, String name, SubCommandRename subCommandRename, boolean inheritOptions) {
        this(dad, name, inheritOptions);
        if (subCommandRename!=null) {
            this.mainCommand = subCommandRename.newName().replaceAll(" ","");
            if (this.mainCommand.contains(" ")) throw new IllegalStateException("Spaces are not allowed in the @SubCommandRename");
        }
    }

    TythanCommandBuilder overloadInvoke() {
        return subCommand("").noHelp();
    }

    public TythanCommandBuilder subCommand(String name) {
        return subCommand(name, true,true);
    }

    public TythanCommandBuilder subCommand(String name, boolean inheritOptions,boolean showInHelp) {
        return new TythanCommandBuilder(this, name, inheritOptions);
    }
    public TythanCommandBuilder subCommand(String name, HelpMessage help, boolean inheritOptions,boolean showInHelp) {
        return new TythanCommandBuilder(this, name,help, inheritOptions);
    }
    public TythanCommandBuilder subCommand(String name, HelpMessage help, SubCommandRename subCommandRename, boolean inheritOptions,boolean showInHelp) {
        return new TythanCommandBuilder(this, name,help,subCommandRename,inheritOptions);
    }
    public TythanCommandBuilder subCommand(String name, SubCommandRename subCommandRename, boolean inheritOptions,boolean showInHelp) {
        return new TythanCommandBuilder(this, name,subCommandRename,inheritOptions);
    }

    public ArgumentBuilder arg(String name) {
        return arg().name(name);
    }

    public ArgumentBuilder arg() {
        if(noMoreArgs) throw new IllegalStateException("This command cannot accept additional arguments.");
        return new ArgumentBuilder(this);
    }

    void addArg(CommandArgument<?> arg) {
        if(arg.hasDefaultInput()) argsHaveDefaults = true;
        else if(argsHaveDefaults) throw new IllegalStateException("For command" + this.mainCommand + ": argument at " + (args.size()-1) + " had no default but previous arguments do");
        args.add(arg);
    }

    public ArgumentBuilder flag(String name,boolean showTab, String... aliases) {
        Validate.isTrue(!"sudo".equals(name), "The flag name 'sudo' is reserved for custom sender args");
        return CommandFlag.make(this, name,showTab, aliases);
    }

    public ArgumentBuilder restrictedFlag(String name, String pex,boolean showTab, String... aliases) {
        Validate.isTrue(!"sudo".equals(name), "The flag name 'sudo' is reserved for custom sender args");
        return CommandFlag.make(this, name, pex,showTab, aliases);
    }

    void addFlag(CommandFlag flag) {
        if(useFlags) flags.add(flag);
        else Tythan.get().getLogger().info("Ignoring flag addition due to noFlags set: " + flag.getName());
    }

    public TythanCommandBuilder noFlags() {
        this.flags.clear();
        useFlags = false;
        return this;
    }

    public TythanCommandBuilder alias(String... aliases) {
        for(String alias : aliases) this.aliases.add(alias.toLowerCase());
        return this;
    }

    public boolean requiresSender() {
        return senderType != null;
    }

    public TythanCommandBuilder requiresSender(Class<?> senderClass) {
        if(requiresSender()) throw new IllegalStateException("Specified sender argument twice for command " + this.mainCommand());

        if(ParameterType.senderTypeExists(senderClass)) {
            senderType = ParameterType.getCustomType(senderClass);
            if(useFlags && (senderType.mapper() != null || senderType.mapperWithSender() != null) ) {
                TythanInstanceProvider.debug(Level.INFO,"Sender can also be mapped by means of a 'sudo' flag");
                ArgumentBuilder b = CommandFlag.make(this, "sudo", "tythan.sudo",false, new String[0]);
                b.description("Sudo another player.");
                b.asType(senderType.getTargetType());
            }
        } else {
            throw new IllegalStateException("This class cannot be used as a command sender: " + senderClass.getSimpleName());
        }

        return this;
    }

    public TythanCommandBuilder noHelp() {
        buildHelpFile = false;
        return this;
    }

    public TythanCommandBuilder build() {
        boolean noneSpecified = payload == null;
        if(noneSpecified) {
            if(!args.isEmpty() || subCommands.isEmpty())
                throw new IllegalStateException("Found no execution sequence for command: " + this.mainCommand
                        + ". This is only possible if the command has subcommands and no arguments specified."
                        + " It is VERY likely the command was built incorrectly.");
            payload = TythanCommand.NULL_COMMAND;
        }
        TythanCommand built = new TythanCommand(
                mainCommand,
                Collections.unmodifiableSet(aliases),
                description,
                permission,
                dontShowInHelp, hiddenCommand,
                senderType,
                Collections.unmodifiableList(args),
                Collections.unmodifiableList(flags),
                Collections.unmodifiableList(subCommands),
                help_message,
                helpRunCommand,
                payload);

        if(built.isInvokeOverload() && !built.getSubCommands().isEmpty())
            throw new IllegalStateException("Found subcommands on an invoke overload for " + this.parentBuilder.mainCommand);

        if(parentBuilder != null) {
            if(built.collides(parentBuilder.subCommands))
                throw new IllegalStateException("Detected ambiguous subcommand: "
                        + built.getMainCommand() + ". Aliases and argument range overlap with other commands!");
            parentBuilder.subCommands.add(built);
        } else {
            val overloads = this.subCommands.stream().filter(TythanCommand::isInvokeOverload).collect(Collectors.toList());
            if(overloads.stream().anyMatch(built::argRangeOverlaps))
                throw new IllegalStateException("Detected ambiguous overloads: "
                        + built.getMainCommand() + ". argument range overlap at the top level!");
        }

        if(buildHelpFile) {
            HelpCommand help = new HelpCommand(built);
            this.subCommands.add(help);
            if(noneSpecified) payload = c->help.runHelp(c, 0);
            if(useFlags) flag("h",false).description("Get help and subcommands").defaultInput("0").asInt();
        }

        //If there's no more builders up the chain we've reached the top. Means we're done and we can make an executor
        if(parentBuilder == null) registrationHandler.accept(built);

        return parentBuilder;
    }


}
