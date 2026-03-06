package com.dsw.practica02.empleados.service;

import com.dsw.practica02.empleados.domain.Empleado;
import com.dsw.practica02.empleados.dto.EmpleadoCreateRequest;
import com.dsw.practica02.empleados.dto.EmpleadoMapper;
import com.dsw.practica02.empleados.dto.EmpleadoResponse;
import com.dsw.practica02.empleados.dto.EmpleadoUpdateRequest;
import com.dsw.practica02.empleados.repository.EmpleadoRepository;
import com.dsw.practica02.empleados.service.exception.ClaveDuplicadaException;
import com.dsw.practica02.empleados.service.exception.EmpleadoNotFoundException;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpleadoService.class);

    private static final String CREATED_COUNTER = "api.empleados.alta";

    private final EmpleadoRepository empleadoRepository;
    private final MeterRegistry meterRegistry;
    private final PasswordEncoder passwordEncoder;

    public EmpleadoService(
            EmpleadoRepository empleadoRepository,
            MeterRegistry meterRegistry,
            PasswordEncoder passwordEncoder
    ) {
        this.empleadoRepository = empleadoRepository;
        this.meterRegistry = meterRegistry;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmpleadoResponse registerEmpleado(EmpleadoCreateRequest request) {
        String claveNormalizada = normalizeClave(request.clave());
        if (empleadoRepository.existsByClaveIgnoreCase(claveNormalizada)) {
            throw new ClaveDuplicadaException(claveNormalizada);
        }
        Empleado empleado = buildEmpleado(request, claveNormalizada);
        Empleado saved = empleadoRepository.save(empleado);
        meterRegistry.counter(CREATED_COUNTER).increment();
        LOGGER.info("Empleado registrado clave={} requestId={}", saved.getClave(), currentRequestId());
        return EmpleadoMapper.toResponse(saved);
    }

    @Transactional
    public Page<EmpleadoResponse> listEmpleados(Pageable pageable) {
        Pageable resolvedPageable = pageable.getSort().isSorted()
                ? pageable
                : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("clave").ascending());
        return empleadoRepository.findAll(resolvedPageable).map(EmpleadoMapper::toResponse);
    }

    @Transactional
    public EmpleadoResponse getEmpleadoById(UUID id) {
        Empleado empleado = findByIdOrThrow(id);
        return EmpleadoMapper.toResponse(empleado);
    }

    @Transactional
    public EmpleadoResponse updateEmpleado(UUID id, EmpleadoUpdateRequest request) {
        Empleado empleado = findByIdOrThrow(id);
        String claveNormalizada = normalizeClave(request.clave());
        empleadoRepository.findByClaveIgnoreCase(claveNormalizada)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ClaveDuplicadaException(claveNormalizada);
                });

        empleado.setClave(claveNormalizada);
        empleado.setNombre(request.nombre().trim());
        empleado.setDireccion(request.direccion().trim());
        empleado.setTelefono(request.telefono().trim());
        empleado.setPassword(passwordEncoder.encode(request.password().trim()));

        Empleado saved = empleadoRepository.save(empleado);
        return EmpleadoMapper.toResponse(saved);
    }

    @Transactional
    public void deleteEmpleado(UUID id) {
        Empleado empleado = findByIdOrThrow(id);
        empleadoRepository.delete(empleado);
    }

    private Empleado buildEmpleado(EmpleadoCreateRequest request, String claveNormalizada) {
        Empleado empleado = new Empleado();
        empleado.setClave(claveNormalizada);
        empleado.setNombre(request.nombre().trim());
        empleado.setDireccion(request.direccion().trim());
        empleado.setTelefono(request.telefono().trim());
        empleado.setPassword(passwordEncoder.encode(request.password().trim()));
        return empleado;
    }

    private String normalizeClave(String clave) {
        return clave == null ? null : clave.trim().toUpperCase(Locale.ROOT);
    }

    private Empleado findByIdOrThrow(UUID id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));
    }

    private String currentRequestId() {
        String requestId = MDC.get("requestId");
        return requestId != null ? requestId : "N/A";
    }
}
