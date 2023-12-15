package com.github.archemedes.tythan.bukkit.menu;

import com.github.archemedes.tythan.bukkit.convo.chatstream.ChatStream;
import com.github.archemedes.tythan.utils.collections.Context;

import java.util.function.Consumer;

public class MenuChatStream extends ChatStream {
    private final MenuAgent agent;

    public MenuChatStream(MenuAgent a) {
        super(a.getPlayer());
        agent = a;
    }

    @Override
    public void activate(Consumer<Context> go) {
        Consumer<Context> chained = c->{
            agent.mergeContext(c);
            go.accept(c);
            agent.getPlayer().openInventory(agent.getMenu().getInventory());
        };

        super.activate(chained);
    }

}
