package com.github.archemedes.tythan.agnostic;

import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.stream.Stream;

public interface Sender {
    boolean hasPermission(String perm);
    String getName();
    @Nullable UUID getUUID();
    boolean isPlayer();
    void sendMessage(String msg);
    void sendMessage(Component msg);
    void sendMessage(AbstractKyoriComponentBuilder msg);

    void sendMessage(UUID uuid,String msg);

    void sendCommand(String command);

    void sendRichMessage(String mini_message);
    void sendRichMessage(UUID uuid,String mini_message);

    @Nullable Object getPlayer();
    default void sendMessage(Component... msg) {Stream.of(msg).forEach(this::sendMessage);}

}
