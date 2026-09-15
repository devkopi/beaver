package dev.ccoding.beaver.command;


import dev.ccoding.beaver.permission.Permissions;
import dev.ccoding.beaver.scheduler.MaintenanceScheduler;
import dev.ccoding.beaver.utils.TimeParser;
import dev.ccoding.beaver.services.MaintenanceService;
import dev.ccoding.beaver.services.MessageService;

import dev.ccoding.beaver.utils.Colors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.sql.Time;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MaintenanceCommand implements TabExecutor {

    private final MaintenanceService maintenanceService;
    private final MessageService messageService;
    private final MaintenanceScheduler maintenanceScheduler;
    private static final String ADMIN_PERMISSION = Permissions.ADMIN;

    // Creo el comando utilizando el estado actual del mantenimiento
    public MaintenanceCommand(MaintenanceService maintenanceService, MessageService messageService, MaintenanceScheduler maintenanceScheduler) {
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
        this.maintenanceScheduler = maintenanceScheduler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        handleAction(sender, args);

        return true;
    }

    // Sugerimos los comandos disponibles del comando
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList(
                    "on",
                    "off",
                    "status",
                    "schedule"
            );
        }
        return Collections.emptyList();
    }

    private void handleAction(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return;
        }

        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage(messageService.get("no-permission"));
            return;
        }

        String action = args[0].toLowerCase();

        switch (action) {

            case "on":
                maintenanceService.enable();

                sender.sendMessage(messageService.get("maintenance.enabled"));
                break;

            case "off":
                maintenanceService.disable();

                sender.sendMessage(messageService.get("maintenance.disabled"));
                break;

            case "status":

                String status = maintenanceService.isEnabled()
                        ? messageService.getRaw("maintenance.status.enabled")
                        : messageService.getRaw("maintenance.status.disabled");

                sender.sendMessage(messageService.getRaw("prefix") + " Status: " + status);
                break;

            case "schedule":
                if (args.length < 2 ) {
                    sender.sendMessage(Colors.color("&6&lBeaver &7» &cUsage: /maintenance schedule <time>"));
                    break;
                }

                long seconds = TimeParser.parse(args[1]);

                if (seconds <= 0) {
                    sender.sendMessage(Colors.color("&6&lBeaver &7» &cInvalid time. Use formats like 30s, 15m or 2h."));
                    break;
                }

                maintenanceScheduler.schedule(seconds);
                sender.sendMessage(
                        Colors.color("&6&lBeaver &7» &aMaintenance scheduled in &f" + args[1] + "&a.")
                );
                break;

            default:
                sendHelp(sender);
                break;
        }
    }

    // Info basica de las funciones del comando /maintenance
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Colors.color("&8&m----------------------------------------"));
        sender.sendMessage(Colors.color("        &6&lBeaver &8» &7Maintenance Commands"));
        sender.sendMessage("");
        sender.sendMessage(Colors.color("&e/maintenance on &8- &7Enable maintenance mode."));
        sender.sendMessage(Colors.color("&e/maintenance off &8- &7Disable maintenance mode."));
        sender.sendMessage(Colors.color("&e/maintenance status &8- &7View the current status."));
        sender.sendMessage(Colors.color("&8&m----------------------------------------"));
    }

}
