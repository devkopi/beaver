package dev.ccoding.beaver.scheduler;

import dev.ccoding.beaver.Beaver;
import dev.ccoding.beaver.services.MaintenanceService;
import dev.ccoding.beaver.services.MessageService;
import dev.ccoding.beaver.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

// Encargado de programar el inicio del mantenimiento
public final class MaintenanceScheduler {
    private final Beaver plugin;
    private final MaintenanceService maintenanceService;
    private final MessageService messageService;

    private BukkitTask task;
    private long endTime;

    // Inicializo el scheduler con sus dependencias necesarias
    public MaintenanceScheduler(Beaver plugin, MaintenanceService maintenanceService, MessageService messageService) {
        this.plugin = plugin;
        this.maintenanceService = maintenanceService;
        this.messageService = messageService;
    }

    // Inicio un nuevo scheduler
    public void schedule(long seconds) {
        endTime = System.currentTimeMillis() + (seconds * 1000);

        // Cancelo tarea previa si ya había una en ejecución
        if (task != null) {
            task.cancel();
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                long remaining = getRemainingSeconds();

                // Verifico si el tiempo programado ha finalizado
                if (remaining <= 0) {
                    maintenanceService.enable();

                    // Notifico por chat que el mantenimiento ha comenzado oficialmente
                    Bukkit.broadcastMessage(messageService.getRaw("prefix") + " " + messageService.getRaw("maintenance.enabled"));

                    cancel();
                    task = null;
                    return;
                }

                // Envío avisos en chat en intervalos clave para mantener informados a los usuarios
                if (remaining == 60 || remaining == 30 || remaining == 15 || remaining == 10) {
                    Bukkit.broadcastMessage(messageService.getRaw("prefix") + " §eServer entering maintenance in §f" + remaining + " seconds§e!");
                }

                // Muestro títulos y subtítulos compatibles con todas las versiones durante los últimos 5 segundos
                if (remaining <= 5 && remaining >= 1) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        TitleUtils.sendTitle(
                                player,
                                ChatColor.RED + "" + ChatColor.BOLD + "MAINTENANCE",
                                ChatColor.YELLOW + "Starting in " + remaining + "s...",
                                0, 25, 5
                        );
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    // Compruebo si actualmente existe un scheduler activo
    public boolean isScheduleActive() {
        return task != null;
    }

    // Obtengo el tiempo restante en segundos
    public long getRemainingSeconds() {
        if (!isScheduleActive()) {
            return 0;
        }

        return Math.max(0, (endTime - System.currentTimeMillis()) / 1000);
    }
}