package com.github.archemedes.tythan.bukkit.menu.icons;

import com.github.archemedes.tythan.bukkit.menu.MenuAction;
import com.github.archemedes.tythan.bukkit.menu.MenuAgent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@RequiredArgsConstructor

public class SimpleButton extends Button {
    ItemStack itemStack;
    Consumer<MenuAction> doThis;

    @Override
    public ItemStack getItemStack(MenuAgent agent) {
        return itemStack;
    }

    @Override
    public void click(MenuAction action) {
        doThis.accept(action);
    }

}
