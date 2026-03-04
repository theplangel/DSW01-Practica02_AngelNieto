ios/ or android/
# Implementation Plan: CRUD de empleados

**Branch**: 001-crud-empleados | **Date**: 2026-02-25 | **Spec**: [specs/001-crud-empleados/spec.md](specs/001-crud-empleados/spec.md)
**Input**: Feature specification from [specs/001-crud-empleados/spec.md](specs/001-crud-empleados/spec.md)

## Summary

Entrega de un servicio REST con Spring Boot 3/Java 17 que permite CRUD completo de la entidad `Empleado`, almacenada en PostgreSQL mediante Spring Data JPA y migraciones Flyway. El API se protege con Basic Auth para un único rol administrativo, valida que `clave` empiece con `E-` seguido de caracteres alfanuméricos en mayúsculas (hasta 100), garantiza unicidad insensible a mayúsculas y documenta los contratos con Springdoc OpenAPI.

## Technical Context

**Language/Version**: Java 17 con Spring Boot 3.2.x  
**Primary Dependencies**: Spring Web, Spring Data JPA, Spring Security, Flyway, Springdoc OpenAPI  
**Storage**: PostgreSQL 15 administrado vía Spring Data JPA con migraciones Flyway en `src/main/resources/db/migration`  
**Testing**: JUnit 5 + Spring Boot Test + Testcontainers PostgreSQL para integración  
**Target Platform**: Servidor Linux desplegado vía Docker Compose (servicio API + base de datos)  
**Project Type**: Web-service REST monolítico  
**Performance Goals**: p95 < 200 ms lecturas y < 400 ms escrituras hasta 10k registros; throughput ≥ 50 req/s sostenidos  
**Constraints**: Contenedor API ≤ 512 MB RAM, Basic Auth obligatorio, validaciones Bean Validation y manejo centralizado de errores  
**Scale/Scope**: Decenas de usuarios administrativos concurrentes, hasta 50k empleados

## Constitution Check

*GATE: Debe pasar antes de la investigación y se revalida tras el diseño.*

| Gate | Status | Evidencia |
|------|--------|-----------|
| Stack Spring Boot 3 + Java 17 | PASS | Plan confirma Java 17/Spring Boot 3.2 y capas controller/service/repository. |
| Seguridad por defecto (Basic Auth) | PASS | NFR-001 exige autenticación básica para rol administrativo único y se configura en Spring Security. |
| Persistencia PostgreSQL + migraciones | PASS | TC-001 y la estrategia Flyway aseguran PostgreSQL con control de versiones. |
| Entorno reproducible Docker | PASS | Quickstart describe docker compose para API y DB. |
| API documentada con Swagger/OpenAPI | PASS | Se incluye Springdoc y contratos en `contracts/`. |

**Post-design check**: PASS — data-model, contratos y quickstart mantienen los principios de stack, seguridad, persistencia, Docker y documentación.

## Project Structure

### Documentation (este feature)

```text
specs/001-crud-empleados/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── employees-api.md
└── tasks.md  # Se generará en /speckit.tasks
```

### Source Code (repository root)

```text
src/main/java/com/dsw/practica02/empleados/
├── config/
├── controller/
├── domain/
├── dto/
├── repository/
└── service/

src/main/resources/
├── application.properties
└── db/migration/

src/test/java/com/dsw/practica02/empleados/
├── controller/
└── service/
```

**Structure Decision**: Se usa un único proyecto Spring Boot; el código del feature vive bajo `com.dsw.practica02.empleados` con pruebas espejo y migraciones Flyway en `src/main/resources/db/migration`.

## Complexity Tracking

No hay violaciones a la constitución; no se requiere justificación adicional.
