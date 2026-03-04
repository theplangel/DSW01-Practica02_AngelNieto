package com.dsw.practica02.empleados.service.exception;

public class ClaveDuplicadaException extends RuntimeException {

    public ClaveDuplicadaException(String clave) {
        super("Ya existe un empleado con clave %s".formatted(clave));
    }
}
