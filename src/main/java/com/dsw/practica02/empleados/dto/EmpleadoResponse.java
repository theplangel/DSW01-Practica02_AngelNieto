package com.dsw.practica02.empleados.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record EmpleadoResponse(
        UUID id,
        String clave,
        String nombre,
        String direccion,
        String telefono,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
