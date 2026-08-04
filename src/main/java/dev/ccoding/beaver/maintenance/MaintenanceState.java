package dev.ccoding.beaver.maintenance;

// Almacenamos el estado actual del mantenimiento
public final class MaintenanceState {
    private boolean enabled;

    // Comprobamos si el mantenimiento está activo
    public boolean isEnabled() {
        return enabled;
    }

    // Cambio el estado del mantenimiento
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
