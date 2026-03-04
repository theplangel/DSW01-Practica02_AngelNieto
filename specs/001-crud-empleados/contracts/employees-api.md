# Employees API Contract

- **Base URL**: `/api/v1/empleados`
- **Authentication**: HTTP Basic. Solo usuarios con rol `ADMIN` pueden invocar los endpoints.
- **Media Type**: `application/json`
- **Error Format**:
  ```json
  {
    "timestamp": "2026-02-25T14:00:00Z",
    "path": "/api/v1/empleados/E-001",
    "status": 404,
    "error": "NOT_FOUND",
    "message": "Empleado no encontrado"
  }
  ```

## Create employee
- **Method/Path**: `POST /api/v1/empleados`
- **Request Body**:
  ```json
  {
    "clave": "E-001",
    "nombre": "Ana Pérez",
    "direccion": "Av. Central 123",
    "telefono": "555-0101"
  }
  ```
- **Responses**:
  - `201 Created` + payload del empleado creado (incluye timestamps si se exponen).
  - `400 Bad Request` si falla validación (por ejemplo, `clave` que no cumple el prefijo `E-` + alfanumérico o campos vacíos/largos).
  - `409 Conflict` si `clave` ya existe (comparación insensible a mayúsculas).

## List employees
- **Method/Path**: `GET /api/v1/empleados`
- **Query Params**: ninguno en esta iteración.
- **Response `200 OK`**:
  ```json
  [
    {
      "clave": "E-001",
      "nombre": "Ana Pérez",
      "direccion": "Av. Central 123",
      "telefono": "555-0101"
    }
  ]
  ```
- Devuelve arreglo vacío si no hay registros.

## Get employee by clave
- **Method/Path**: `GET /api/v1/empleados/{clave}`
- **Path Variable**: `clave` normalizada a mayúsculas e interpretada con prefijo `E-`.
- **Responses**:
  - `200 OK` con el objeto `Empleado`.
  - `404 Not Found` si la clave no existe.

## Update employee
- **Method/Path**: `PUT /api/v1/empleados/{clave}`
- **Request Body**:
  ```json
  {
    "nombre": "Ana P.",
    "direccion": "Av. Central 321",
    "telefono": "555-0202"
  }
  ```
- **Responses**:
  - `200 OK` con el recurso actualizado.
  - `400 Bad Request` si la longitud supera 100 caracteres o campos vienen vacíos.
  - `404 Not Found` si la clave no existe.

## Delete employee
- **Method/Path**: `DELETE /api/v1/empleados/{clave}`
- **Responses**:
  - `204 No Content` al eliminar físicamente el registro.
  - `404 Not Found` si la clave no existe.

## Audit & headers
- Toda respuesta exitosa incluye `ETag` basado en `updated_at` para facilitar futuros GET condicionales.
- Se recomienda incluir `X-Request-Id` en cada solicitud/response para trazabilidad (controlado por filtro común).
