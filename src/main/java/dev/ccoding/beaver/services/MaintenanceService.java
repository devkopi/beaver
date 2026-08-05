package dev.ccoding.beaver.services;

import dev.ccoding.beaver.Beaver;
import dev.ccoding.beaver.maintenance.MaintenanceState;
import dev.ccoding.beaver.permission.Permissions;
import dev.ccoding.beaver.services.MessageService;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// Ejecuta las acciones principales relacionadas con el sistema de mantenimiento.
public final class MaintenanceService {
    private final Beaver plugin;
    private final MaintenanceState maintenanceState;
    private final MessageService messageService;

    public MaintenanceService(Beaver plugin, MaintenanceState maintenanceState, MessageService messageService) {
        this.plugin = plugin;
        this.maintenanceState = maintenanceState;
        this.messageService = messageService;
    }

    // Activo el modo mantenimiento
    public void enable() {

        if (maintenanceState.isEnabled()) {
            return;
        }

        maintenanceState.setEnabled(true);
        kickPlayers();
    }

    // Desactivo el modo mantenimiento
    public void disable() {
        if (!maintenanceState.isEnabled()) {
            return;
        }

        maintenanceState.setEnabled(false);
    }

    // Compruebo si el mantenimiento está activo
    public boolean isEnabled() {
        return maintenanceState.isEnabled();
    }

    // Expulso a todos los jugadores que no tengan permiso para permanecer durante el mantenimiento
    private void kickPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(Permissions.BYPASS)) {
                continue;
            }

            player.kickPlayer(
                    messageService.getRaw("maintenance.kick-message")

            );
        }
    }
}
