package com.dsw.practica02.empleados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpleadoCreateRequest(
        @NotBlank(message = "La clave es obligatoria")
        @Pattern(regexp = "(?i)E-[A-Z0-9]{1,97}", message = "La clave debe iniciar con E- seguido de hasta 97 caracteres alfanuméricos")
        String clave,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no debe superar 100 caracteres")
        String nombre,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 100, message = "La dirección no debe superar 100 caracteres")
        String direccion,

        @NotBlank(message = "El teléfono es obligatorio")
        @Size(max = 100, message = "El teléfono no debe superar 100 caracteres")
        String telefono,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
        String password
) {
}
