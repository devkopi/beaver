package dev.ccoding.beaver.command;

import dev.ccoding.beaver.permission.Permissions;
import dev.ccoding.beaver.services.MessageService;
import dev.ccoding.beaver.utils.Colors;
import dev.ccoding.beaver.Beaver;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public final class BeaverCommand implements CommandExecutor {

    private final Beaver plugin;
    private final MessageService messageService;

    public BeaverCommand(Beaver plugin, MessageService messageService) {
        this.plugin = plugin;
        this.messageService = messageService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendInformation(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission(Permissions.ADMIN)) {
                sender.sendMessage(messageService.get("no-permission"));
                return true;
            }
            plugin.reloadPlugin();

            sender.sendMessage(messageService.get("plugin.reloaded"));
            return true;
        }
        sendInformation(sender);
        return true;
    }

    private void sendInformation(CommandSender sender) {
        sender.sendMessage(Colors.color("&6&lBeaver &8┃ &7Smart Maintenance System"));
        sender.sendMessage(Colors.color(""));
        sender.sendMessage(Colors.color("&7Version: " + plugin.getDescription().getVersion()));
        sender.sendMessage(Colors.color("&7Author: " + plugin.getDescription().getAuthors().get(0)));
        sender.sendMessage(Colors.color(""));
        sender.sendMessage(Colors.color("&eCommands:"));
        sender.sendMessage(Colors.color("&6/maintenance &8- &7Maintenance system."));
    }
}
