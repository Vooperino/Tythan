package com.github.archemedes.tythan.bukkit.convo.chatstream;

import com.github.archemedes.tythan.utils.collections.Context;
import lombok.experimental.UtilityClass;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

@UtilityClass
public class ClickStream {
    public static void selectBlock(Player p, Consumer<Block> callback) {
        selectBlock(p, callback, b->true);
    }

    public static void selectBlock(Player p, Consumer<Block> callback, Predicate<Block> filter) {
        Consumer<Context> pipe = c->callback.accept(c.get("target"));
        new ChatStream(p)
                .clickBlockPrompt("target", "Please RIGHT click a block",filter)
                .activate(pipe);
    }

    public static void selectPlayer(Player p, Consumer<Player> callback) {
        selectEntity(p, e->callback.accept((Player) e), Player.class::isInstance);
    }

    public static void selectEntity(Player p, Consumer<Entity> callback) {
        selectEntity(p, callback, e->true);
    }

    public static void selectEntity(Player p, Consumer<Entity> callback, Predicate<Entity> filter) {
        Consumer<Context> pipe = c->callback.accept(c.get("target"));
        new ChatStream(p)
                .clickEntityPrompt("target", "Please RIGHT click an entity",filter)
                .activate(pipe);
    }

}
