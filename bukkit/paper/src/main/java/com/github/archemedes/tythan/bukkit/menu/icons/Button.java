package com.github.archemedes.tythan.bukkit.menu.icons;

import org.bukkit.inventory.ItemStack;

public abstract class Button extends Icon {
    @Override
    public final boolean mayInteract(ItemStack moved) {
        return false;
    }

}
