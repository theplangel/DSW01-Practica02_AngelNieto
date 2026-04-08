package com.dsw.practica02.empleados.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dsw.practica02.empleados.domain.Empleado;
import com.dsw.practica02.empleados.dto.EmpleadoCreateRequest;
import com.dsw.practica02.empleados.dto.EmpleadoResponse;
import com.dsw.practica02.empleados.repository.EmpleadoRepository;
import com.dsw.practica02.empleados.service.exception.ClaveDuplicadaException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmpleadoService empleadoService;

    private EmpleadoCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new EmpleadoCreateRequest("e-001", "Ana", "Centro", "555", "secreto123");
    }

    @Test
    void registerEmpleado_shouldPersistAndReturnResponse() {
        when(meterRegistry.counter("api.empleados.alta")).thenReturn(mock(Counter.class));
        when(empleadoRepository.existsByClaveIgnoreCase("E-001")).thenReturn(false);
        when(passwordEncoder.encode("secreto123")).thenReturn("{noop}secreto123");
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> {
            Empleado entity = invocation.getArgument(0);
            entity.setId(java.util.UUID.randomUUID());
            entity.setCreatedAt(OffsetDateTime.now());
            entity.setUpdatedAt(OffsetDateTime.now());
            return entity;
        });

        EmpleadoResponse response = empleadoService.registerEmpleado(createRequest);

        assertThat(response.clave()).isEqualTo("E-001");
        assertThat(response.nombre()).isEqualTo("Ana");
    }

    @Test
    void registerEmpleado_shouldThrowWhenClaveExists() {
        when(empleadoRepository.existsByClaveIgnoreCase("E-001")).thenReturn(true);

        assertThatThrownBy(() -> empleadoService.registerEmpleado(createRequest))
                .isInstanceOf(ClaveDuplicadaException.class);
    }
}
