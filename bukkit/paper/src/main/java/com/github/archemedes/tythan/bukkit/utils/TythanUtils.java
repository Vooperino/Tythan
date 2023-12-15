package com.github.archemedes.tythan.bukkit.utils;

import com.github.archemedes.tythan.bukkit.TythanBukkit;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TythanUtils {
    public static boolean brigadierVersionValidation() {
        if (TythanBukkit.get().getMcVerHandler()==null) return false;
        return TythanBukkit.get().getMcVerHandler().isBrokenBrigadier();
    }
}
