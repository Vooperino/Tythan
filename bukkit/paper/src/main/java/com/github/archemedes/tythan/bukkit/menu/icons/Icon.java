package com.github.archemedes.tythan.bukkit.menu.icons;

import com.github.archemedes.tythan.bukkit.menu.Menu;
import com.github.archemedes.tythan.bukkit.menu.MenuAction;
import com.github.archemedes.tythan.bukkit.menu.MenuAgent;
import org.bukkit.inventory.ItemStack;

public abstract class Icon {
    Icon(){}
    public abstract ItemStack getItemStack(MenuAgent agent);
    public abstract void click(MenuAction action);
    public boolean mayInteract(ItemStack moved) { return false; }
    public void updateItem(Menu menu) {
        menu.updateIconItem(this);
    }

}
