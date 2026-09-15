package dev.ccoding.beaver.utils;

import dev.ccoding.beaver.services.MessageService;
import org.bukkit.configuration.file.FileConfiguration;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class DiscordWebhook {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    // Envía la alerta de Discord de forma dinámica según la ruta que le pases
    public static void sendAlert(FileConfiguration config, MessageService messageService, String pathPrefix, String timePlaceholder) {
        if (!config.getBoolean("discord.enabled", false)) return;

        String webhookUrl = config.getString("discord.webhook-url");
        if (webhookUrl == null || webhookUrl.isEmpty() || webhookUrl.contains("LINK_WEBHOOK_HERE")) return;

        boolean sendEmbed = config.getBoolean("discord.send-embed", true);
        boolean useEveryone = Boolean.parseBoolean(messageService.getRaw(pathPrefix + ".use-everyone"));
        String rawContent = messageService.getRaw(pathPrefix + ".content");

        String content = useEveryone ? rawContent : rawContent.replace("@everyone", "").replace("@here", "");
        if (timePlaceholder != null) {
            content = content.replace("%time%", timePlaceholder);
        }

        String jsonBody;

        if (sendEmbed) {
            String title = messageService.getRaw(pathPrefix + ".embed.title");
            String description = messageService.getRaw(pathPrefix + ".embed.description");

            if (timePlaceholder != null) {
                description = description.replace("%time%", timePlaceholder);
            }

            // Obtenemos el color de forma segura
            String colorStr = messageService.getRaw(pathPrefix + ".embed.color");
            int colorInt = 16734296; // Color por defecto (por si acaso)
            try {
                if (colorStr != null && !colorStr.contains(".")) {
                    colorInt = Integer.parseInt(colorStr.replace("#", ""), 16);
                }
            } catch (NumberFormatException ignored) {
                // Si falla la conversión, mantiene el color por defecto y evita que critee el servidor
            }

            String imageUrl = messageService.getRaw(pathPrefix + ".embed.image-url");
            String thumbnailUrl = messageService.getRaw(pathPrefix + ".embed.thumbnail-url");
            String footer = messageService.getRaw(pathPrefix + ".embed.footer");

            StringBuilder embedBuilder = new StringBuilder();
            embedBuilder.append("{")
                    .append("\"title\": \"").append(escapeJson(title)).append("\",")
                    .append("\"description\": \"").append(escapeJson(description)).append("\",")
                    .append("\"color\": ").append(colorInt);

            if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.contains("image-url")) {
                embedBuilder.append(",\"image\": {\"url\": \"").append(escapeJson(imageUrl)).append("\"}");
            }
            if (thumbnailUrl != null && !thumbnailUrl.isEmpty() && !thumbnailUrl.contains("thumbnail-url")) {
                embedBuilder.append(",\"thumbnail\": {\"url\": \"").append(escapeJson(thumbnailUrl)).append("\"}");
            }
            if (footer != null && !footer.isEmpty() && !footer.contains("footer")) {
                embedBuilder.append(",\"footer\": {\"text\": \"").append(escapeJson(footer)).append("\"}");
            }

            embedBuilder.append("}");

            jsonBody = "{"
                    + "\"content\": \"" + escapeJson(content) + "\","
                    + "\"embeds\": [" + embedBuilder.toString() + "]"
                    + "}";
        } else {
            jsonBody = "{\"content\": \"" + escapeJson(content) + "\"}";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(webhookUrl))
                .header("Content-Type", "application/json")
                .header("User-Agent", "Beaver-Plugin")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        // Envío asíncrono seguro
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.discarding())
                .exceptionally(ex -> null);
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}