package com.github.archemedes.tythan.utils;

import lombok.experimental.UtilityClass;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.RGBLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
@SuppressWarnings("unused")
public class ColorUtil {
    public static @NotNull TextColor darker(TextColor origin) {return lerp(0.15f, origin, NamedTextColor.BLACK);}
    public static @NotNull TextColor darken(TextColor toColor, float byFactor) {return lerp(byFactor, toColor, NamedTextColor.BLACK);}
    public static @NotNull TextColor lighter(TextColor origin) {return lerp(0.15f, origin, NamedTextColor.WHITE);}
    public static @NotNull TextColor lighten(TextColor toColor, float byFactor) {return lerp(byFactor, toColor, NamedTextColor.WHITE);}

    public static @NotNull TextColor complementary(TextColor ofColor) {return rotateHue(ofColor, 180.0f);}

    public static @NotNull TextColor rotateHue(@NotNull TextColor textColor, float degree) {
        HSVLike hsv = textColor.asHSV();
        var deg = hsv.h() * 360.0f + degree;
        if (deg > 360) deg -= 360;
        else if (deg<0)deg += 360;
        return TextColor.color(HSVLike.hsvLike(deg / 360.0f, hsv.s(), hsv.v()));
    }

    public static @NotNull TextColor lerp(final float t, final @NotNull RGBLike a, final @NotNull RGBLike b) {
        final float clampedT = Math.min(1.0f, Math.max(0.0f, t));
        final int ar = a.red();
        final int br = b.red();
        final int ag = a.green();
        final int bg = b.green();
        final int ab = a.blue();
        final int bb = b.blue();
        return TextColor.color(
                Math.round(ar + clampedT * (br - ar)),
                Math.round(ag + clampedT * (bg - ag)),
                Math.round(ab + clampedT * (bb - ab))
        );
    }

    public static @NotNull @Unmodifiable TextColor saturate(@NotNull TextColor color, float byValue) {
        HSVLike hsv = color.asHSV();
        float s = Math.min(1, Math.max(0, hsv.s() + byValue));
        return TextColor.color(HSVLike.hsvLike(hsv.h(), s, hsv.v()));
    }

    public static @NotNull @Unmodifiable TextColor addBrightness(@NotNull TextColor toColor, float byValue) {
        HSVLike hsv = toColor.asHSV();
        float v = Math.min(1, Math.max(0, hsv.v() + byValue));
        return TextColor.color(HSVLike.hsvLike(hsv.h(), hsv.s(), v));
    }

    public static @NotNull @Unmodifiable List<TextColor> analogous(TextColor ofColor) {
        return List.of(
                rotateHue(ofColor, 30),
                rotateHue(ofColor, -30)
        );
    }

    public static @NotNull @Unmodifiable List<TextColor> neutral(TextColor ofColor) {
        return List.of(
                rotateHue(ofColor, 15),
                rotateHue(ofColor, -15)
        );
    }

    public static @NotNull @Unmodifiable List<TextColor> triadicComplement(TextColor ofColor) {
        return List.of(
                rotateHue(ofColor, 120),
                rotateHue(ofColor, -120)
        );
    }

    public static @NotNull @Unmodifiable List<TextColor> splitComplementary(TextColor ofColor) {
        return List.of(
                rotateHue(ofColor, 150),
                rotateHue(ofColor, 320)
        );
    }

    public static double getLumas(TextColor color) {
        int red = color.red();
        int green = color.green();
        int blue = color.blue();
        var luma = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
        return luma;
    }

    public static boolean isColorLight(TextColor color) {
        return getLumas(color) > 210;
    }

    public static boolean isColorDark(TextColor color) {
        return getLumas(color) < 40;
    }

}
