package dev.ccoding.beaver;

import dev.ccoding.beaver.command.BeaverCommand;
import dev.ccoding.beaver.command.MaintenanceCommand;
import dev.ccoding.beaver.maintenance.MaintenanceState;
import dev.ccoding.beaver.listener.MaintenanceListener;
import dev.ccoding.beaver.services.MaintenanceService;
import dev.ccoding.beaver.services.MessageService;

import org.bukkit.plugin.java.JavaPlugin;

public final class Beaver extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        maintenanceState = new MaintenanceState();
        maintenanceService = new MaintenanceService(maintenanceState);
        messageService = new MessageService(this);

        registerCommands();
        registerListeners();

        getLogger().info("Beaver has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Beaver has been Disabled.");
    }

    // Registro de comandos
    private void registerCommands(){
        getCommand("maintenance").setExecutor(new MaintenanceCommand(maintenanceService, messageService));
        getCommand("beaver").setExecutor(new BeaverCommand(this, messageService));
    }

    // Registro de eventos
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new MaintenanceListener(maintenanceState, messageService), this);
    }

    // Recargo todos los archivos de configuración del plugin
    public void reloadPlugin() {
        reloadConfig();
        messageService.reload();
    }

    // Estado actual del sistema de mantenimiento
    private MaintenanceState maintenanceState;

    private MaintenanceService maintenanceService;

    private MessageService messageService;
}
