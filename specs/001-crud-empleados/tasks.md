# Tasks: CRUD de empleados

**Input**: Design documents from `/specs/001-crud-empleados/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: Constitución del proyecto exige pruebas automáticas; cada historia incluye tareas de pruebas antes de la implementación.

**Organization**: Tasks are grouped por historia de usuario para habilitar entregas independientes.

## Phase 1: Setup (Shared Infrastructure)

**Propósito**: Garantizar dependencias, configuración base y entorno reproducible.

- [ ] T001 Actualizar `pom.xml` con Spring Web, Spring Data JPA, Spring Security, Validation, Flyway, Springdoc y dependencias de prueba (JUnit 5, Testcontainers PostgreSQL).
- [ ] T002 Crear `docker-compose.yml` en la raíz con servicios `api` y `db` (PostgreSQL 15) alineados al quickstart.
- [ ] T003 Configurar `src/main/resources/application.properties` para perfiles `dev/test`, datasource PostgreSQL y credenciales Basic Auth leídas de variables de entorno.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Propósito**: Infraestructura común antes de abordar historias.

- [ ] T004 Crear `src/main/resources/db/migration/V1__create_empleados_table.sql` con tabla `empleados`, índice único `upper(clave)` y columnas `created_at/updated_at`.
- [ ] T005 [P] Implementar entidad `Empleado` en `src/main/java/com/dsw/practica02/empleados/domain/Empleado.java` con normalización de `clave` y auditoría básica.
- [ ] T006 [P] Definir `EmpleadoRepository` en `src/main/java/com/dsw/practica02/empleados/repository/EmpleadoRepository.java` con consultas por `upper(clave)`.
- [ ] T007 [P] Crear DTO `EmpleadoResponse` y `EmpleadoMapper` en `src/main/java/com/dsw/practica02/empleados/dto/` para exponer campos públicos.
- [ ] T008 [P] Configurar Basic Auth en `src/main/java/com/dsw/practica02/empleados/config/SecurityConfig.java` (credenciales externas, rol `ADMIN`, protección `/api/v1/empleados/**`).
- [ ] T009 [P] Implementar `GlobalExceptionHandler` en `src/main/java/com/dsw/practica02/empleados/config/GlobalExceptionHandler.java` para mapear 400/404/409 con el formato de errores del contrato.
- [ ] T010 Preparar base de pruebas con Testcontainers en `src/test/java/com/dsw/practica02/empleados/AbstractIntegrationTest.java` inicializando `PostgreSQLContainer` y Flyway.

**Checkpoint**: Infraestructura lista; se pueden abordar historias en paralelo.

---

## Phase 3: User Story 1 - Registrar empleado (Priority: P1) 🎯 MVP

**Goal**: Permitir crear empleados con `clave` prefijada `E-`, validando duplicados y longitudes.

**Independent Test**: Ejecutar `POST /api/v1/empleados` con payload válido y verificar persistencia + rechazo de duplicados via pruebas automáticas.

### Tests (US1)

- [ ] T011 [P] [US1] Añadir casos de creación/duplicado en `src/test/java/com/dsw/practica02/empleados/service/EmpleadoServiceTest.java`.
- [ ] T012 [P] [US1] Añadir pruebas de integración para `POST /api/v1/empleados` en `src/test/java/com/dsw/practica02/empleados/controller/EmpleadoControllerIntegrationTest.java` (usa Testcontainers).

### Implementation (US1)

- [ ] T013 [P] [US1] Crear `EmpleadoCreateRequest` con Bean Validation (`clave` patrón `E-[A-Z0-9]{1,97}`) en `src/main/java/com/dsw/practica02/empleados/dto/EmpleadoCreateRequest.java`.
- [ ] T014 [P] [US1] Implementar `EmpleadoService.registerEmpleado()` en `src/main/java/com/dsw/practica02/empleados/service/EmpleadoService.java` (normaliza `clave`, aplica validaciones y gestiona 409).
- [ ] T015 [US1] Exponer `POST /api/v1/empleados` en `src/main/java/com/dsw/practica02/empleados/controller/EmpleadoController.java` con `@Operation` y respuestas 201/400/409.
- [ ] T016 [US1] Registrar eventos y métricas básicas del alta en `src/main/java/com/dsw/practica02/empleados/service/EmpleadoService.java` usando logs y `X-Request-Id` propagado.

**Parallel Example (US1)**

```
T011 || T012  # pruebas en paralelo
T013 || T014  # DTO y servicio en paralelo antes del controlador
```

**Checkpoint**: El endpoint de registro funciona y pasa pruebas.

---

## Phase 4: User Story 2 - Consultar empleados (Priority: P2)

**Goal**: Listar empleados y recuperar uno por `clave` con respuestas ordenadas consistentes.

**Independent Test**: Ejecutar `GET /api/v1/empleados` y `GET /api/v1/empleados/{clave}` contra datos semilla verificando 200 y 404.

### Tests (US2)

- [ ] T017 [P] [US2] Agregar pruebas de servicio para listados y búsqueda por clave en `src/test/java/com/dsw/practica02/empleados/service/EmpleadoServiceTest.java`.
- [ ] T018 [P] [US2] Agregar pruebas de integración para `GET /api/v1/empleados` y `GET /api/v1/empleados/{clave}` en `src/test/java/com/dsw/practica02/empleados/controller/EmpleadoControllerIntegrationTest.java`.

### Implementation (US2)

- [ ] T019 [P] [US2] Añadir métodos `findAllOrdered` y `findByClave` en `src/main/java/com/dsw/practica02/empleados/service/EmpleadoService.java` reutilizando `EmpleadoMapper`.
- [ ] T020 [US2] Implementar endpoints `GET /api/v1/empleados` y `GET /api/v1/empleados/{clave}` en `src/main/java/com/dsw/practica02/empleados/controller/EmpleadoController.java` con paginación futura preparada y manejo de 404.
- [ ] T021 [US2] Optimizar `EmpleadoRepository` en `src/main/java/com/dsw/practica02/empleados/repository/EmpleadoRepository.java` con métodos de ordenamiento por `clave` y proyecciones ligeras.

**Parallel Example (US2)**

```
T017 || T018  # pruebas GET en paralelo
T019 || T021  # service y repository pueden avanzar juntos antes del controlador
```

**Checkpoint**: Consultas listas para validar datos existentes.

---

## Phase 5: User Story 3 - Actualizar y eliminar empleado (Priority: P3)

**Goal**: Actualizar campos editables y eliminar registros físicamente respetando reglas de validación.

**Independent Test**: Ejecutar `PUT /api/v1/empleados/{clave}` seguido de `DELETE /api/v1/empleados/{clave}` verificando 200/204 y 404 para claves inexistentes.

### Tests (US3)

- [ ] T022 [P] [US3] Cubrir update/delete en `src/test/java/com/dsw/practica02/empleados/service/EmpleadoServiceTest.java` (valida longitudes y 404).
- [ ] T023 [P] [US3] Añadir pruebas de integración para `PUT` y `DELETE` en `src/test/java/com/dsw/practica02/empleados/controller/EmpleadoControllerIntegrationTest.java`.

### Implementation (US3)

- [ ] T024 [P] [US3] Crear `EmpleadoUpdateRequest` (solo nombre/dirección/teléfono) en `src/main/java/com/dsw/practica02/empleados/dto/EmpleadoUpdateRequest.java` con Bean Validation.
- [ ] T025 [US3] Implementar `updateEmpleado` y `deleteEmpleado` en `src/main/java/com/dsw/practica02/empleados/service/EmpleadoService.java` asegurando `updated_at` y manejo de inexistentes.
- [ ] T026 [US3] Exponer `PUT /api/v1/empleados/{clave}` y `DELETE /api/v1/empleados/{clave}` en `src/main/java/com/dsw/practica02/empleados/controller/EmpleadoController.java` con códigos 200/204/404.

**Parallel Example (US3)**

```
T022 || T023  # pruebas en paralelo
T024 || T025  # DTO y servicio antes del controlador
```

**Checkpoint**: CRUD completo disponible y probado.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Propósito**: Mejoras finales y verificación del flujo end-to-end.

- [ ] T027 [P] Añadir `OpenApiConfig` en `src/main/java/com/dsw/practica02/empleados/config/OpenApiConfig.java` con metadatos y servidor por perfil.
- [ ] T028 [P] Implementar `RequestIdFilter` en `src/main/java/com/dsw/practica02/empleados/config/RequestIdFilter.java` para propagar `X-Request-Id` y registrarlo en logs.
- [ ] T029 Ejecutar flujo de `specs/001-crud-empleados/quickstart.md`, ajustar instrucciones y registrar resultados en el mismo archivo si hay cambios.

---

## Dependencies & Execution Order

- Phase 1 → Phase 2 → User stories → Phase 6. Ninguna historia puede iniciar sin completar Phase 2.
- Historias pueden desarrollarse en paralelo tras Phase 2, pero se recomienda orden P1 → P2 → P3 para MVP.
- Dentro de cada historia: pruebas (T011/T012, T017/T018, T022/T023) deben escribirse antes de implementar controladores/servicios correspondientes.
- T021 depende de T006 para ampliar repositorio; T020 depende de T019; T026 depende de T025.

## Parallel Execution Examples por Historia

- **US1**: (T011 ∥ T012), (T013 ∥ T014) antes de T015.
- **US2**: (T017 ∥ T018), (T019 ∥ T021) antes de T020.
- **US3**: (T022 ∥ T023), (T024 ∥ T025) antes de T026.

## Implementation Strategy

1. **MVP (US1)**: Completar Fases 1-2 y US1; validar altas con pruebas e integración dockerizada.
2. **Incremental**: Añadir US2 manteniendo independencia; publicar release parcial para consultas.
3. **Completar CRUD**: Implementar US3 y ejecutar regresión rápida.
4. **Polish**: Aplicar Phase 6, revisar documentación y quickstart.
