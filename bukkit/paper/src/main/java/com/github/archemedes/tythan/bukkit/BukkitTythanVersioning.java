package com.github.archemedes.tythan.bukkit;

import com.github.archemedes.tythan.bukkit.nms.TythanBukkitNMS;
import com.github.archemedes.tythan.bukkit.nms.v1_19_4.TythanNMSV1_19_4;
import com.github.archemedes.tythan.bukkit.nms.v1_20.TythanNMSV1_20;
import com.github.archemedes.tythan.bukkit.nms.v1_20_2.TythanNMSV1_20_2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter @RequiredArgsConstructor
public enum BukkitTythanVersioning {

    v1_19_R1(new TythanNMSV1_19_4(),true,false),
    v1_19_R2(new TythanNMSV1_19_4(),true,false),
    v1_19_R3(new TythanNMSV1_19_4(),true,false),

    v1_20_R1(new TythanNMSV1_20(),true,true),
    v1_20_R2(new TythanNMSV1_20_2(),true,true),
    ;

    private final TythanBukkitNMS tythanNMS;
    private final boolean weirdCommandBuildContext;
    private final boolean brokenBrigadier;

    @Nullable
    public static BukkitTythanVersioning getVersionHandler(@NotNull String nmsVersion) {
        for (BukkitTythanVersioning ver : BukkitTythanVersioning.values()) if (ver.name().equals(nmsVersion)) return ver;
        return null;
    }

    @Nullable public static BukkitTythanVersioning getVersionHandler() {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        return getVersionHandler(ver.substring(ver.lastIndexOf('.') + 1));
    }

}
