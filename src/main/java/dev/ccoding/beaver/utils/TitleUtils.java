package dev.ccoding.beaver.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

// Utilidad para enviar títulos compatible con múltiples versiones (1.8.8 a la actual)
public final class TitleUtils {

    private static boolean useModernTitle = true;
    private static Method modernSendTitleMethod;

    static {
        // Verificamos una sola vez al cargar la clase si el servidor soporta el método moderno de la API
        try {
            modernSendTitleMethod = Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            useModernTitle = false;
        }
    }

    private TitleUtils() {
        // No es necesario lanzar la excepción
    }

    // Envío un título y subtítulo a un jugador usando el método optimizado según la compatibilidad detectada
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (useModernTitle && modernSendTitleMethod != null) {
            try {
                // Ejecución directa ultrarrápida para servidores modernos
                modernSendTitleMethod.invoke(player, title, subtitle, fadeIn, stay, fadeOut);
                return;
            } catch (Exception ignored) {
            }
        }

        // Fallback por reflexión exclusivo para servidores clásicos como 1.8.8
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            Class<?> chatSerializer = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
            Class<?> chatComponent = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
            Class<?> packetPlayOutChat = Class.forName("net.minecraft.server." + version + ".PacketPlayOutTitle");
            Class<?> enumAction = Class.forName("net.minecraft.server." + version + ".PacketPlayOutTitle$EnumTitleAction");

            Object chatTitle = chatSerializer.getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
            Object chatSubtitle = chatSerializer.getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");

            Constructor<?> constructor = packetPlayOutChat.getConstructor(enumAction, chatComponent, int.class, int.class, int.class);

            Object objTitle = constructor.newInstance(enumAction.getField("TITLE").get(null), chatTitle, fadeIn, stay, fadeOut);
            Object objSubtitle = constructor.newInstance(enumAction.getField("SUBTITLE").get(null), chatSubtitle, fadeIn, stay, fadeOut);

            sendPacket(player, objTitle);
            sendPacket(player, objSubtitle);
        } catch (Exception ex) {
            player.sendMessage(title + " - " + subtitle);
        }
    }

    // Método auxiliar para enviar paquetes NMS de manera segura en versiones legacy
    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object connection = handle.getClass().getField("playerConnection").get(handle);
            connection.getClass().getMethod("sendPacket", Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".Packet")).invoke(connection, packet);
        } catch (Exception ignored) {
        }
    }
}