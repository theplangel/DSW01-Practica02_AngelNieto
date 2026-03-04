# Quickstart – CRUD de empleados

## Prerrequisitos
- Java 17 y Maven Wrapper (`./mvnw`).
- Docker y Docker Compose 2.x.
- Variables de entorno definidas:
  - `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB`.
  - `ADMIN_USERNAME`, `ADMIN_PASSWORD` para el rol administrativo del API.

## Pasos
1. **Levantar PostgreSQL**
   ```bash
   docker compose up db -d
   ```
   Verifica que el contenedor `db` exponga el puerto `5432`.
2. **Aplicar migraciones Flyway**
   ```bash
   ./mvnw -Dflyway.configFiles=flyway.conf flyway:migrate
   ```
   (Alternativamente, el arranque del API ejecutará las migraciones automáticamente si la config está en `application.properties`).
3. **Ejecutar la API**
   ```bash
   ./mvnw spring-boot:run
   ```
   El servicio queda disponible en `http://localhost:8080`.
4. **Probar autenticación**
   ```bash
   curl -u "$ADMIN_USERNAME:$ADMIN_PASSWORD" http://localhost:8080/api/v1/empleados
   ```
   Debe responder `200 OK` con arreglo vacío si no hay datos.
5. **Probar flujo CRUD completo**
   ```bash
   curl -u "$ADMIN_USERNAME:$ADMIN_PASSWORD" \
      -H 'Content-Type: application/json' \
      -d '{"clave":"E-001","nombre":"Ana","direccion":"Centro","telefono":"555"}' \
      -X POST http://localhost:8080/api/v1/empleados
   ```
   Luego consulta `GET /api/v1/empleados/E-001` y `DELETE /api/v1/empleados/E-001` para completar el ciclo.

## Verificaciones adicionales
- Abre `http://localhost:8080/swagger-ui.html` para revisar la documentación expuesta por Springdoc.
- Ejecuta pruebas automáticas:
  ```bash
  ./mvnw test
  ```
  Las pruebas de integración arrancarán un contenedor PostgreSQL mediante Testcontainers.
