package com.github.archemedes.tythan.bukkit.menu.icons;

import com.github.archemedes.tythan.bukkit.menu.Menu;
import com.github.archemedes.tythan.bukkit.menu.MenuAction;
import com.github.archemedes.tythan.bukkit.menu.MenuAgent;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)

public class Link extends Button {
    Supplier<Menu> to;
    ItemStack item;

    public Link(ItemStack is, Menu menu) {
        to = Suppliers.ofInstance(menu);
        item = is;
    }

    public Link(ItemStack is, Supplier<Menu> supplier) {
        to = supplier;
        item = is;
    }

    @Contract("_, _ -> new")
    public static @NotNull Link memoize(ItemStack is, Supplier<Menu> supplier) {
        return new Link(is, Suppliers.memoize(supplier));
    }

    @Override
    public ItemStack getItemStack(MenuAgent agent) {
        return item;
    }

    @Override
    public void click(@NotNull MenuAction action) {
        action.getMenuAgent().open(to.get());
    }

}
