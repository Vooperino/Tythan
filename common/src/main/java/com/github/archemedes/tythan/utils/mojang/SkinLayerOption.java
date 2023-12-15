package com.github.archemedes.tythan.utils.mojang;

public record SkinLayerOption(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftPants, boolean rightPants, boolean head) {
    public byte toBytesFlags() {
        return (byte) ((cape() ? SkinOptions.Flags.CAPE.flag : SkinOptions.Flags.NOTHING.flag) |
                (jacket() ? SkinOptions.Flags.JACKET.flag : SkinOptions.Flags.NOTHING.flag) |
                (leftSleeve() ? SkinOptions.Flags.LEFT_SLEEVE.flag : SkinOptions.Flags.NOTHING.flag) |
                (rightSleeve() ? SkinOptions.Flags.RIGHT_SLEEVE.flag : SkinOptions.Flags.NOTHING.flag) |
                (leftPants() ? SkinOptions.Flags.LEFT_PANTS.flag : SkinOptions.Flags.NOTHING.flag) |
                (rightPants() ? SkinOptions.Flags.RIGHT_PANTS.flag : SkinOptions.Flags.NOTHING.flag) |
                (head() ? SkinOptions.Flags.HEAD.flag : SkinOptions.Flags.NOTHING.flag));
    }
}

