package com.dsw.practica02.empleados.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dsw.practica02.empleados.AbstractIntegrationTest;
import com.dsw.practica02.empleados.repository.EmpleadoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class EmpleadoControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String BASE_URL = "/api/v1/empleados";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @AfterEach
    void cleanUp() {
        empleadoRepository.deleteAll();
    }

    @Test
    void createEmpleado_shouldReturnCreated() throws Exception {
                String payload = objectMapper.writeValueAsString(new CreatePayload("E-010", "Luis", "Centro", "555", "secret123"));

        mockMvc.perform(post(BASE_URL)
                .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clave").value("E-010"));
    }

    @Test
    void createEmpleado_shouldReturnConflictWhenDuplicate() throws Exception {
                String payload = objectMapper.writeValueAsString(new CreatePayload("E-020", "Ana", "Centro", "555", "secret123"));

        mockMvc.perform(post(BASE_URL)
                .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated());

        mockMvc.perform(post(BASE_URL)
                .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isConflict());
    }

        private record CreatePayload(String clave, String nombre, String direccion, String telefono, String password) {
    }
}
