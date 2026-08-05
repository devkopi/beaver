package dev.ccoding.beaver;

import dev.ccoding.beaver.command.BeaverCommand;
import dev.ccoding.beaver.command.MaintenanceCommand;
import dev.ccoding.beaver.listener.ServerListListener;
import dev.ccoding.beaver.maintenance.MaintenanceState;
import dev.ccoding.beaver.listener.MaintenanceListener;
import dev.ccoding.beaver.services.MaintenanceService;
import dev.ccoding.beaver.services.MessageService;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Beaver extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        maintenanceState = new MaintenanceState();
        messageService = new MessageService(this);
        maintenanceService = new MaintenanceService(this, maintenanceState, messageService);

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
        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new MaintenanceListener(maintenanceService, messageService), this);
        pluginManager.registerEvents(new ServerListListener(this, maintenanceService), this);

        //getServer().getPluginManager().registerEvents(new MaintenanceListener(maintenanceState, messageService), this);
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
