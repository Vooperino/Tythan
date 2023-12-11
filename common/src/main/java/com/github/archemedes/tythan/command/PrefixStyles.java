package com.github.archemedes.tythan.command;

import com.github.archemedes.tythan.agnostic.CommonKyoriComponentBuilder;
import com.github.archemedes.tythan.utils.TColor;
import net.kyori.adventure.text.Component;

public interface PrefixStyles {
    Component ERROR_PREFIX = new CommonKyoriComponentBuilder().append(new CommonKyoriComponentBuilder().append("[✘]",TColor.ERROR_RED).hoverText(new CommonKyoriComponentBuilder().append("Error",TColor.ERROR_RED))).space().build();
    Component INFO_PREFIX = new CommonKyoriComponentBuilder().append(new CommonKyoriComponentBuilder().append("[ℹ]",TColor.BLUE).hoverText(new CommonKyoriComponentBuilder().append("Information",TColor.BLUE))).space().build();
    Component SUCCESS_PREFIX = new CommonKyoriComponentBuilder().append(new CommonKyoriComponentBuilder().append("[✓]",TColor.GREEN).hoverText(new CommonKyoriComponentBuilder().append("Success",TColor.GREEN))).space().build();
    Component FAILURE_PREFIX = new CommonKyoriComponentBuilder().append(new CommonKyoriComponentBuilder().append("[⚠]",TColor.DARK_RED).hoverText(new CommonKyoriComponentBuilder().append("Failure",TColor.DARK_RED))).space().build();
    Component WARNING_PREFIX = new CommonKyoriComponentBuilder().append(new CommonKyoriComponentBuilder().append("[⚠]",TColor.WARNING_YELLOW).hoverText(new CommonKyoriComponentBuilder().append("Failure",TColor.WARNING_YELLOW))).space().build();
    Component DEBUG_PREFIX = Component.text("[DEBUG]: ", TColor.PURPLE);

}
