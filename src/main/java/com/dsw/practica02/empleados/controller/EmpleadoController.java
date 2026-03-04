package com.dsw.practica02.empleados.controller;

import com.dsw.practica02.empleados.dto.EmpleadoCreateRequest;
import com.dsw.practica02.empleados.dto.EmpleadoResponse;
import com.dsw.practica02.empleados.dto.EmpleadoUpdateRequest;
import com.dsw.practica02.empleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/empleados")
@Validated
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Listar empleados")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> listAll() {
        return ResponseEntity.ok(empleadoService.listEmpleados());
    }

    @Operation(summary = "Obtener empleado por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(empleadoService.getEmpleadoById(id));
    }

    @Operation(summary = "Registrar un nuevo empleado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado"),
            @ApiResponse(responseCode = "409", description = "Conflicto por clave duplicada")
    })
    @PostMapping
    public ResponseEntity<EmpleadoResponse> create(@Valid @RequestBody EmpleadoCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.registerEmpleado(request));
    }

    @Operation(summary = "Actualizar empleado por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto por clave duplicada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody EmpleadoUpdateRequest request
    ) {
        return ResponseEntity.ok(empleadoService.updateEmpleado(id, request));
    }

    @Operation(summary = "Eliminar empleado por id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sin contenido"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        empleadoService.deleteEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}
