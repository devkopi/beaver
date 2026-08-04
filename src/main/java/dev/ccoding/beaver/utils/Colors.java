package dev.ccoding.beaver.utils;

import org.bukkit.ChatColor;

public final class Colors {

    private Colors() {
        // Evita que esta clase pueda instanciarse.
    }

    // Traduzco los código de color de un mensaje
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
