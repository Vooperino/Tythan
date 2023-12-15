package com.github.archemedes.tythan.bukkit.command;

import com.github.archemedes.tythan.agnostic.Sender;
import com.github.archemedes.tythan.bukkit.wrapper.BukkitSender;
import com.github.archemedes.tythan.command.CommandArgument;
import com.github.archemedes.tythan.command.CommandCompleter;
import com.github.archemedes.tythan.command.TythanCommand;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TythanCommandSuggestionProvider<T> implements SuggestionProvider<T> {
    private final CommandArgument<T> arg;
    private final TythanCommand cmd;

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<T> context, SuggestionsBuilder builder) {
        T source = context.getSource();
        Sender sender = new BukkitSender(BrigadierProvider.get().getBukkitSender(source));
        for(CommandCompleter.Suggestion suggestion : arg.getCompleter().suggest(sender, builder.getRemaining())) {
            if (!cmd.hasPermission(sender)) continue;
            String sugg = suggestion.getLiteral();
            if (sugg.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                if(suggestion.hasTooltip()) builder.suggest(sugg, new LiteralMessage(suggestion.getTooltip()) );
                else builder.suggest(sugg);
            }
        }
        return builder.buildFuture();

    }
}
