package com.github.archemedes.tythan.bukkit.menu.icons;

import com.github.archemedes.tythan.bukkit.menu.MenuAction;
import com.github.archemedes.tythan.bukkit.menu.MenuAgent;
import com.github.archemedes.tythan.bukkit.utils.ItemUtil;
import com.github.archemedes.tythan.utils.TColor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public final class Pad extends Icon {
    @Getter private final ItemStack itemStack;

    public Pad(Material m) {
        itemStack = ItemUtil.make(m, Component.text("", TColor.GRAY));
    }

    @Override
    public void click(MenuAction action) {}

    @Override
    public ItemStack getItemStack(MenuAgent agent) {
        return getItemStack();
    }

}
