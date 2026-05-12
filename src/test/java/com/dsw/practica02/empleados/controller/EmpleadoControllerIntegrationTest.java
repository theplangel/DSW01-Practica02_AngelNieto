package com.dsw.practica02.empleados.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dsw.practica02.empleados.config.GlobalExceptionHandler;
import com.dsw.practica02.empleados.dto.EmpleadoResponse;
import com.dsw.practica02.empleados.service.EmpleadoService;
import com.dsw.practica02.empleados.service.exception.ClaveDuplicadaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerIntegrationTest {

    private static final String BASE_URL = "/api/v1/empleados";

    @Mock
    private EmpleadoService empleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createEmpleado_shouldReturnCreated() throws Exception {
        EmpleadoResponse response = new EmpleadoResponse(
                UUID.randomUUID(),
                "E-010",
                "Luis",
                "Centro",
                "555",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        org.mockito.Mockito.when(empleadoService.registerEmpleado(org.mockito.ArgumentMatchers.any()))
                .thenReturn(response);

        String payload = objectMapper.writeValueAsString(
                new CreatePayload("E-010", "Luis", "Centro", "555", "secreto123")
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("E-010"));
    }

    @Test
    void createEmpleado_shouldReturnConflictWhenDuplicate() throws Exception {
        org.mockito.Mockito.when(empleadoService.registerEmpleado(org.mockito.ArgumentMatchers.any()))
                .thenThrow(new ClaveDuplicadaException("E-020"));

        String payload = objectMapper.writeValueAsString(
                new CreatePayload("E-020", "Ana", "Centro", "555", "secreto123")
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict());
    }

    private record CreatePayload(String clave, String nombre, String direccion, String telefono, String password) {
    }
}
