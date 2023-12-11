package com.github.archemedes.tythan.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.math.BigInteger;
import java.util.UUID;
import java.util.regex.Pattern;


@UtilityClass
public final class UUIDUtils {
    @Unmodifiable public static final Pattern uuidRegex = Pattern.compile("[a-f0-9]{8}(?:-[a-f0-9]{4}){4}[a-f0-9]{8}");

    public static boolean isUUID(@NotNull String input) {
        return uuidRegex.matcher(input).matches();
    }
    public static String RepairUUIDLine(String input) {
        String s = input;
        String s2 = s.replace("-", "");
        UUID uuid = new UUID(
                new BigInteger(s2.substring(0, 16), 16).longValue(),
                new BigInteger(s2.substring(16), 16).longValue());
        return uuid.toString();
    }
    public static UUID RepairUUIDLineToUUID(String input) {
        String s = input;
        String s2 = s.replace("-", "");
        return new UUID(
                new BigInteger(s2.substring(0, 16), 16).longValue(),
                new BigInteger(s2.substring(16), 16).longValue());
    }

    @Nullable public static UUID parseFromStringSafe(@NotNull String data) {
        try {
            return UUID.fromString(data);
        } catch (Exception ignored) {}
        return null;
    }

}
