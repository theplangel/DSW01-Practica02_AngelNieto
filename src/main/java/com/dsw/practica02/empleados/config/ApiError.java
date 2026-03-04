package com.dsw.practica02.empleados.config;

import java.time.OffsetDateTime;

public record ApiError(
        OffsetDateTime timestamp,
        String path,
        int status,
        String error,
        String message
) {
}
