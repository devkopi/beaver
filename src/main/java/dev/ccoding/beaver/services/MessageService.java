package dev.ccoding.beaver.services;

import dev.ccoding.beaver.Beaver;
import dev.ccoding.beaver.utils.Colors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public final class MessageService {
    private final Beaver plugin;
    private FileConfiguration messages;

    public MessageService(Beaver plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    // Cargo el archivo messages.yml
    private void loadMessages() {
        File file = new File(plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);
    }

    // Obtengo un mensaje usando su ruta dentro del archivo
    public String get(String path) {
        return getRaw("prefix") + " " + getRaw(path);
    }

    // Obtengo un mensaje sin el prefijo
    public String getRaw(String path) {
        String message = messages.getString(path, path);

        return Colors.color(message);
    }

    public Beaver getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return messages;
    }

    // Recargo el archivo messages.yml
    public void reload() {
        loadMessages();
    }
}
