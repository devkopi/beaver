package dev.ccoding.beaver.services;

import dev.ccoding.beaver.maintenance.MaintenanceState;

// Ejecuta las acciones principales relacionadas con el sistema de mantenimiento.
public final class MaintenanceService {
    private final MaintenanceState maintenanceState;

    public MaintenanceService(MaintenanceState maintenanceState) {
        this.maintenanceState = maintenanceState;
    }

    // Activo el modo mantenimiento
    public void enable() {
        maintenanceState.setEnabled(true);
    }

    // Desactivo el modo mantenimiento
    public void disable() {
        maintenanceState.setEnabled(false);
    }

    // Compruebo si el mantenimiento está activo
    public boolean isEnabled() {
        return maintenanceState.isEnabled();
    }
}
