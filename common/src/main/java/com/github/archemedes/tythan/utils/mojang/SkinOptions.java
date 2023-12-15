package com.github.archemedes.tythan.utils.mojang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class SkinOptions implements Serializable {
    @Getter private final String skinName, url;

    @Getter private final boolean cape, jacket, left_sleeve, right_sleeve, left_pants, right_pants, hat;

    public SkinOptions(@NotNull String skinName) {
        this.skinName = skinName;
        this.url = null;
        this.cape = false;
        this.jacket = false;
        this.left_sleeve = false;
        this.right_sleeve = false;
        this.left_pants = false;
        this.right_pants = false;
        this.hat = false;
    }

    public SkinOptions(String url, boolean cape, boolean jacket, boolean left_sleeve, boolean right_sleeve, boolean left_pants, boolean right_pants, boolean hat) {
        this.skinName = "custom";
        this.url = url;
        this.cape = cape;
        this.jacket = jacket;
        this.left_sleeve = left_sleeve;
        this.right_sleeve = right_sleeve;
        this.left_pants = left_pants;
        this.right_pants = right_pants;
        this.hat = hat;
    }



    public byte getFlags() {
        return !skinName.equalsIgnoreCase("custom") ? skinName.equalsIgnoreCase("default") | skinName.equalsIgnoreCase("custom2") ?
                (byte) (Flags.CAPE.flag | Flags.JACKET.flag | Flags.LEFT_SLEEVE.flag | Flags.RIGHT_SLEEVE.flag | Flags.LEFT_PANTS.flag | Flags.RIGHT_PANTS.flag | Flags.HEAD.flag) :
                (byte) ((true ? Flags.CAPE.flag : Flags.NOTHING.flag) |
                        (true ? Flags.JACKET.flag : Flags.NOTHING.flag) |
                        (true ? Flags.LEFT_SLEEVE.flag : Flags.NOTHING.flag) |
                        (true ? Flags.RIGHT_SLEEVE.flag : Flags.NOTHING.flag) |
                        (true ? Flags.LEFT_PANTS.flag : Flags.NOTHING.flag) |
                        (true ? Flags.RIGHT_PANTS.flag : Flags.NOTHING.flag) |
                        (true ? Flags.HEAD.flag : Flags.NOTHING.flag)) : (byte) ((cape ? Flags.CAPE.flag : Flags.NOTHING.flag) |
                (jacket ? Flags.JACKET.flag : Flags.NOTHING.flag) |
                (left_sleeve ? Flags.LEFT_SLEEVE.flag : Flags.NOTHING.flag) |
                (right_sleeve ? Flags.RIGHT_SLEEVE.flag : Flags.NOTHING.flag) |
                (left_pants ? Flags.LEFT_PANTS.flag : Flags.NOTHING.flag) |
                (right_pants ? Flags.RIGHT_PANTS.flag : Flags.NOTHING.flag) |
                (hat ? Flags.HEAD.flag : Flags.NOTHING.flag));
    }

    @AllArgsConstructor
    @Getter
    public enum Flags {
        NOTHING((byte) 0x00),
        CAPE((byte) 0x01),
        JACKET((byte) 0x02),
        LEFT_SLEEVE((byte) 0x04),
        RIGHT_SLEEVE((byte) 0x08),
        LEFT_PANTS((byte) 0x10),
        RIGHT_PANTS((byte) 0x20),
        HEAD((byte) 0x40);

        public final byte flag;

    }

}
