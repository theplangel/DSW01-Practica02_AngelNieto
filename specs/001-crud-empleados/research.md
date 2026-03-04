# Research – CRUD de empleados

## Performance envelope for admin CRUD
- Decision: Objetivo de p95 < 200 ms en lecturas y < 400 ms en escrituras con hasta 10k registros activos, limitando los listados completos a lotes pequeños si supera 50k.
- Rationale: El tráfico es bajo pero se necesita una experiencia inmediata; estas cifras son alcanzables con Spring Boot + PostgreSQL sin tuning exótico.
- Alternatives considered: No se definía métrica (descartado por falta de visibilidad) o adoptar colas/eventos (sobreingeniería para la carga prevista).

## Operational constraints and resource caps
- Decision: Contenedor de la API limitado a 512 MB de RAM y 0.5 CPU; toda validación via Bean Validation y manejo centralizado de errores para evitar fugas de información.
- Rationale: Mantener footprint pequeño simplifica despliegue en entornos de práctica y es suficiente para la carga; la validación consistente satisface FR-007/FR-008 y la constitución.
- Alternatives considered: Ejecutar sin límites o aceptar memoria dinámica del host (incrementa costo y dificulta reproducibilidad), o duplicar servicios para escalar horizontalmente (innecesario ahora).

## Spring Data JPA + PostgreSQL best practices
- Decision: Usar entidades anotadas con `@Entity`, repositorios que extienden `JpaRepository`, índices únicos sobre `upper(clave)` y normalización a mayúsculas antes de persistir.
- Rationale: Combina lo indicado en FR-002/FR-011 con las opciones idiomáticas de JPA, reduce código repetitivo y se integra con transacciones declarativas.
- Alternatives considered: JDBC puro (más boilerplate y maneja manualmente transacciones) o repositorios reactivos (no necesarios para este volumen).

## Spring Security Basic Auth for single admin role
- Decision: Configurar Spring Security para exigir HTTP Basic, cargar credenciales desde configuración segura y asociar todas las rutas `/api/v1/empleados/**` al rol `ADMIN`.
- Rationale: Cumple NFR-001 y la constitución con mínima complejidad; Basic Auth es suficiente para este entorno cerrado.
- Alternatives considered: Deshabilitar seguridad (viola la constitución) o implementar OIDC completo (sobrecosto sin múltiples roles/identidades externas).

## Flyway migration strategy
- Decision: Crear `V1__create_empleados_table.sql` con la tabla `empleados` y restricciones, gestionar cambios futuros mediante versiones incrementales.
- Rationale: Mantiene la base sincronizada con el código y respeta la exigencia de migraciones versionadas para PostgreSQL.
- Alternatives considered: DDL manual fuera de control de versiones (riesgo de drift) o Liquibase (válido pero el proyecto ya privilegia Flyway por simplicidad).

## Springdoc OpenAPI contract maintenance
- Decision: Añadir `springdoc-openapi-starter-webmvc-ui` y documentar los endpoints mediante anotaciones `@Operation`, publicando documentación en `/swagger-ui.html`.
- Rationale: Garantiza cumplimiento del principio "API documentada" y permite validar contratos rápidamente.
- Alternatives considered: Documentación manual en Markdown (fácil de desincronizar) o herramientas comerciales (sobre dimensionadas).

## Integration testing with Testcontainers
- Decision: Ejecutar pruebas de integración usando `PostgreSQLContainer` para obtener comportamiento cercano a producción y verificar unicidad/normalización.
- Rationale: Simula PostgreSQL real sin depender de instancias externas y mantiene el pipeline reproducible.
- Alternatives considered: H2 en memoria (no refleja comportamientos específicos de PostgreSQL) o mocks de repositorio (no prueban la capa de persistencia real).
