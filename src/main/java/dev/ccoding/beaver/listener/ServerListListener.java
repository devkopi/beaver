package dev.ccoding.beaver.listener;

import dev.ccoding.beaver.Beaver;
import dev.ccoding.beaver.services.MaintenanceService;
import dev.ccoding.beaver.utils.MotdUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

// Actualiza el MOTD cuando un jugador consulta el servidor
public final class ServerListListener implements Listener {
    private final Beaver plugin;
    private final MaintenanceService maintenanceService;

    public ServerListListener(Beaver plugin, MaintenanceService maintenanceService) {
        this.plugin = plugin;
        this.maintenanceService = maintenanceService;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (!plugin.getConfig().getBoolean("motd.enabled")) {
            return;
        }

        if (maintenanceService.isEnabled()) {
            event.setMotd(MotdUtils.getMaintenanceMotd(plugin));
            return;
        }

        event.setMotd(MotdUtils.getNormalMotd(plugin));
    }

}
