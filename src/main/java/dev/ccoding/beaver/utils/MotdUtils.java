package dev.ccoding.beaver.utils;

import dev.ccoding.beaver.Beaver;

import java.awt.*;
import java.util.List;

// Construimos el MOTD leyendo la configuración del plugin
public final class MotdUtils {

    private MotdUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Obtengo el MOTD normal del servidor
    public static String getNormalMotd(Beaver plugin) {
        return build(plugin, "motd.normal");
    }

    // Obtengo el MOTD del mantenimiento
    public static String getMaintenanceMotd(Beaver plugin) {
        return build(plugin, "motd.maintenance");
    }

    // Construyo el MOTD utilizando la sección del config.yml
    public static String build(Beaver plugin, String path) {
        List<String> lines = plugin.getConfig().getStringList(path);

        if (lines.isEmpty()) {
            return "";
        }

        StringBuilder motd = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            motd.append(Colors.color(lines.get(i)));

            if (i < lines.size() - 1) {
                motd.append("\n");
            }
        }

        return motd.toString();
    }
}
