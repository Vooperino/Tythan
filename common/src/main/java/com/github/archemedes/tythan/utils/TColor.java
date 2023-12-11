package com.github.archemedes.tythan.utils;

import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class TColor implements TextColor {

    //Hard Coded Presets
    public static final TColor AMBER = new TColor(0xFD5912);
    public static final TColor DARK_RED = new TColor(0x870000);
    public static final TColor OCHRE = new TColor(0xCc7722);
    public static final TColor TAUPE = new TColor(0xB38b6d);
    public static final TColor WHITE_SMOKE = new TColor(0xF5f5f5);
    public static final TColor SCARLET = new TColor(0xFf2400);
    public static final TColor SIENNA = new TColor(0xA0522d);
    public static final TColor BURNT_SIENNA = new TColor(0xE97451);
    public static final TColor KELLY_GREEN = new TColor(0x4cbb17);
    public static final TColor ELECTRIC_BLUE = new TColor(0x0592D0);
    public static final TColor AMETHYST = new TColor(0x9966cc);
    public static final TColor BLACK = new TColor(0x1B1B1B);
    public static final TColor PUMPKIN = new TColor(0xFF6D3A);
    public static final TColor  BROWN = new TColor(0x805533);
    public static final TColor AQUAMARINE = new TColor(0x7fffd4);
    public static final TColor TIMBERWOLF = new TColor(0xDbd7d2);
    public static final TColor NAVY_BLUE = new TColor(0x3c37c9);
    public static final TColor GREEN = new TColor(0x3AA655);
    public static final TColor CYAN = new TColor(0x0a94a5);
    public static final TColor LILAC = new TColor(0xDB91EF);
    public static final TColor DANDELION = new TColor(0xFED85D);
    public static final TColor  PURPLE = new TColor(0xaa00aa);
    public static final TColor GOLD = new TColor(0xffaa00);
    public static final TColor GRAY = new TColor(0xaaaaaa);
    public static final TColor DARK_GRAY = new TColor(0x555555);
    public static final TColor BLUE = new TColor(0x5555ff);
    public static final TColor LIME = new TColor(0x55ff55);
    public static final TColor AQUA = new TColor(0x55ffff);
    public static final TColor RED = new TColor(0xff5555);
    public static final TColor YELLOW = new TColor(0xffff55);
    public static final TColor ERROR_RED = new TColor(0xFD0E35);
    public static final TColor WARNING_YELLOW = new TColor(0xFFDF46);
    public static final TColor WARNING_TEXT_YELLOW = new TColor(0xF2C649);
    public static final TColor ERROR_TEXT_RED = new TColor(0xED0A3F);
    public static final TColor MAGENTA = new TColor(0xff55ff);
    public static final TColor SILVER = new TColor(0xC0c0c0);
    public static final TColor PINEAPPLE = new TColor(0xFEEA63);
    public static final TColor PEAR = new TColor(0xD1E231);
    public static final TColor EMERALD = new TColor(0x50C878);
    public static final TColor DEEP_EMERALD = new TColor(0x1B7931);
    public static final TColor PALE_MINT = new TColor(0x9ADBBA);
    public static final TColor MANTIS = new TColor(0x74C365);
    public static final TColor JADE = new TColor(0x00A86B);
    public static final TColor TYROLITE = new TColor(0x00A499);
    public static final TColor NEON_TEAL = new TColor(0x00CAB1);
    public static final TColor TEAL = new TColor(0x008080);
    public static final TColor FLUORESCENT = new TColor(0x00B0BC);
    public static final TColor SPRING_TEAL = new TColor(0x0EA7A5);
    public static final TColor AESTHETIC_TEAL = new TColor(0x0D9494);
    public static final TColor ATLANTIC = new TColor(0x155370);
    public static final TColor ULTRAMARINE = new TColor(0x2142AB);
    public static final TColor AZURE = new TColor(0x0080FF);
    public static final TColor BLUEBERRY = new TColor(0x4F86F7);
    public static final TColor NAVY = new TColor(0x000080);
    public static final TColor SAPPHIRE = new TColor(0x0F52BA);
    public static final TColor LUST = new TColor(0xE62020);
    public static final TColor TANGERINE = new TColor(0xFA9336);
    public static final TColor BRIGHT_PINK = new TColor(0xFF007F);
    public static final TColor HOT_PINK = new TColor(0xFF69B4);
    public static final TColor THULIAN = new TColor(0xDE6FA1);
    public static final TColor WILD_WATERMELON = new TColor(0xFC6C85);
    public static final TColor REBECCA = new TColor(0x663399);
    public static final TColor PATRIARCH = new TColor(0x800080);
    public static final TColor MAUVEINE = new TColor(0x8D029B);
    public static final TColor VERONICA = new TColor(0xA020F0);
    public static final TColor WHITE = new TColor(0xffffff);


    //Code Part
    @Getter private final TextColor color;

    public TColor(@NotNull String HEX_COLOR) {
        if (CommonPatterns.HEX_COLOR.matcher(HEX_COLOR).matches()) {
            this.color = TextColor.fromHexString(HEX_COLOR);
        } else if (CommonPatterns.HEX_COLOR_NOSH.matcher(HEX_COLOR).matches()) {
            this.color = TextColor.fromHexString("#"+HEX_COLOR);
        } else throw new IllegalStateException("Inputed string value must match with the a hex color");
    }

    public TColor(int red, int green, int blue) {
        validateRGBColorValue("red",red);
        validateRGBColorValue("grren",green);
        validateRGBColorValue("blue",blue);
        this.color = TextColor.color(red,green,blue);
    }

    public TColor(int INT_HEXCOLOR) {
        this.color = TextColor.color(INT_HEXCOLOR);
    }

    @Override public int value() {return this.color.value();}
    private static void validateRGBColorValue(String rgb_cname, int rgb_value) {
        if (rgb_value > 255) throw new IllegalStateException(rgb_cname+" cannot go above "+255);
        else if (rgb_value < 0) throw new IllegalStateException(rgb_cname+" cannot go below "+0);
    }

}
