package dev.ccoding.beaver.listener;

import dev.ccoding.beaver.maintenance.MaintenanceState;
import dev.ccoding.beaver.permission.Permissions;
import dev.ccoding.beaver.services.MessageService;
import dev.ccoding.beaver.utils.Colors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

// Controla el acceso a jugadores cuando el sistema de mantenimiento está activo
public final class MaintenanceListener implements Listener {
    private final MaintenanceState maintenanceState;
    private final MessageService messageService;

    public MaintenanceListener(MaintenanceState maintenanceState, MessageService messageService) {
        this.maintenanceState = maintenanceState;
        this.messageService = messageService;
    }

    /**
     * Compruebo si un jugador puede entrar al servidor.
     *
     * Si el mantenimiento está activo y el jugador no tiene
     * permiso de bypass, bloqueo su conexión.
     *
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!maintenanceState.isEnabled()) {
            return;
        }

        if (event.getPlayer().hasPermission(Permissions.BYPASS)) {
            return;
        }

        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, messageService.get("maintenance.kick-message"));
    }
}
