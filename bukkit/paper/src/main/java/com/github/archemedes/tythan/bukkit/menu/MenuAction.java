package com.github.archemedes.tythan.bukkit.menu;

import com.github.archemedes.tythan.bukkit.menu.icons.Icon;
import com.github.archemedes.tythan.bukkit.utils.InventoryUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE) @Getter
public class MenuAction {
    private final MenuAgent menuAgent;
    private final List<InventoryUtil.MovedItem> movedItems;
    private final ClickType click;

    MenuAction(MenuAgent a){
        menuAgent = a;
        movedItems = new ArrayList<>();
        click = ClickType.UNKNOWN;
    }

    public Player getPlayer() {
        return menuAgent.getPlayer();
    }

    public void refreshItem(Icon icon) {
        this.getMenuAgent().getMenu().updateIconItem(icon);
    }

}
