package com.dsw.practica02.empleados.service.exception;

import java.util.UUID;

public class EmpleadoNotFoundException extends RuntimeException {

    public EmpleadoNotFoundException(String clave) {
        super("Empleado con clave %s no encontrado".formatted(clave));
    }

    public EmpleadoNotFoundException(UUID id) {
        super("Empleado con id %s no encontrado".formatted(id));
    }
}
