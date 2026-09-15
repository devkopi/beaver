package dev.ccoding.beaver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Convierte un tiempo escrito por el usuario a segundos
public final class TimeParser {

    private static final Pattern TIME_PATTERN = Pattern.compile("^(\\d+)([smh])$");

    // Q no se pueda instanciar pq esta clase ofrece metodos estaticos
    private TimeParser() {
    }

    // Convierto un tiempo como 30s, 15m o 2h a segundos
    public static long parse(String input) {
        Matcher matcher = TIME_PATTERN.matcher(input.toLowerCase());

        if (!matcher.matches()) {
            return -1;
        }

        long value = Long.parseLong(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "s":
                return value;

            case "m":
                return value * 60;

            case "h":
                return value * 3600;

            default:
                return -1;
        }
    }
}
