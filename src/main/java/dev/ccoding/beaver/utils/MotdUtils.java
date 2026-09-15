package dev.ccoding.beaver.utils;

import dev.ccoding.beaver.Beaver;
import org.bukkit.ChatColor;

import java.util.List;

// Construimos el MOTD leyendo la configuración del plugin
public final class MotdUtils {

    private static final int CENTER_PX = 125; // Ancho estándar en píxeles de la cajita del MOTD

    private MotdUtils() {
        // No es necesario lanzar la excepción
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
            String rawLine = lines.get(i);

            // Verifico si la línea contiene la etiqueta de centrado para aplicar formato simétrico
            if (rawLine.startsWith("<center>") || rawLine.startsWith("{center}")) {
                rawLine = rawLine.replaceFirst("(<center>|\\{center\\})", "");
                rawLine = centerText(rawLine);
            }

            motd.append(Colors.color(rawLine));

            if (i < lines.size() - 1) {
                motd.append("\n");
            }
        }

        return motd.toString();
    }

    // Centro el texto en base al ancho de píxeles caracter por caracter en Minecraft
    private static String centerText(String text) {
        String strippedText = ChatColor.stripColor(Colors.color(text));

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : strippedText.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            }
            if (previousCode) {
                previousCode = false;
                isBold = (c == 'l' || c == 'L');
                continue;
            }

            DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            messagePxSize++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int compensated = 0;

        StringBuilder sb = new StringBuilder();
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;

        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }

        return sb.toString() + text;
    }

    // Definimos el ancho en píxeles de cada fuente estándar de Minecraft
    private enum DefaultFontInfo {
        A('A', 5), a('a', 5), B('B', 5), b('b', 5), C('C', 5), c('c', 5),
        D('D', 5), d('d', 5), E('E', 5), e('e', 5), F('F', 5), f('f', 4),
        G('G', 5), g('g', 5), H('H', 5), h('h', 5), I('I', 3), i('i', 1),
        J('J', 5), j('j', 5), K('K', 5), k('k', 4), L('L', 5), l('l', 1),
        M('M', 5), m('m', 5), N('N', 5), n('n', 5), O('O', 5), o('o', 5),
        P('P', 5), p('p', 5), Q('Q', 5), q('q', 5), R('R', 5), r('r', 5),
        S('S', 5), s('s', 5), T('T', 5), t('4', 4), U('U', 5), u('u', 5),
        V('V', 5), v('v', 5), W('W', 5), w('w', 5), X('X', 5), x('x', 5),
        Y('Y', 5), y('y', 5), Z('Z', 5), z('z', 5),
        SPACE(' ', 3),
        DEFAULT('a', 5);

        private final char character;
        private final int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public int getLength() {
            return length;
        }

        public int getBoldLength() {
            if (this == SPACE) return length;
            return length + 1;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            for (DefaultFontInfo dFI : DefaultFontInfo.values()) {
                if (dFI.character == c) return dFI;
            }
            return DEFAULT;
        }
    }
}