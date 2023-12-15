package com.github.archemedes.tythan.bukkit.menu.icons;

import com.github.archemedes.tythan.bukkit.menu.MenuAction;
import com.github.archemedes.tythan.bukkit.menu.MenuAgent;
import org.bukkit.inventory.ItemStack;

public class SimpleSlot extends Slot {
    private ItemStack item;

    public SimpleSlot() {
        this(null);
    }

    public SimpleSlot(ItemStack item) {
        this.item = item;
    }

    @Override
    public void click(MenuAction action) {}

    @Override
    public ItemStack getItemStack(MenuAgent a) {
        return item;
    }

}
