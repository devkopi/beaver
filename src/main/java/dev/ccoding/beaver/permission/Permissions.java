package dev.ccoding.beaver.permission;

// Almacenar los permisos utilizados por el plugin
public final class Permissions {

    private Permissions() {
        // Evito que esta clase pueda instanciarse.
    }

    // Permiso pricipal para administrar el sistema de mantenimiento.
    public static final String ADMIN = "beaver.admin";

    // Permiso para acceder al servidor durante un mantenimiento
    public static final String BYPASS = "beaver.bypass";
}