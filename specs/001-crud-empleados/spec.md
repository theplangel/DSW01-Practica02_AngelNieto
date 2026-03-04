# Feature Specification: CRUD de empleados

**Feature Branch**: `001-crud-empleados`  
**Created**: 2026-02-25  
**Status**: Draft  
**Input**: User description: "Crea un crud de empleados, con los campos clave, nombre direccion y telefono, clave seria la clave primaria, nombre, direccion y telefono de 100 espacios."

## Clarifications

### Session 2026-02-25

- Q: ¿Qué formato debe tener `clave`? → A: Alfanumérica en mayúsculas, con unicidad insensible a mayúsculas/minúsculas.
- Q: ¿Cómo debe manejarse la eliminación de empleados dentro de este CRUD? → A: La eliminación será física definitiva, sin registros inactivos ni bitácoras especiales.
- Q: ¿Qué esquema de autenticación/autorización aplicará al CRUD? → A: Todas las operaciones requerirán autenticación de un único rol administrativo con acceso completo.
- Q: ¿Dónde deben persistirse los empleados? → A: En la base de datos relacional principal del proyecto, usando una tabla dedicada.
- Q: ¿Qué formato final debe tener `clave` con el prefijo `E-` requerido? → A: Debe iniciar con `E-` seguido de caracteres alfanuméricos mixtos (1-97), manteniendo el límite total de 100 caracteres.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Registrar empleado (Priority: P1)

Como usuario administrativo, quiero registrar un empleado con sus datos obligatorios para contar con un padrón inicial confiable.

**Why this priority**: Sin alta de empleados no existe información para consultar, actualizar o eliminar.

**Independent Test**: Puede probarse de forma independiente creando un empleado válido y verificando que queda disponible para consulta.

**Acceptance Scenarios**:

1. **Given** que no existe un empleado con clave `E-001`, **When** el usuario registra `clave=E-001`, `nombre`, `direccion` y `telefono` válidos, **Then** el sistema confirma el alta y guarda el nuevo empleado.
2. **Given** que ya existe un empleado con clave `E-001`, **When** el usuario intenta registrar nuevamente `clave=E-001`, **Then** el sistema rechaza la operación e informa clave duplicada.

---

### User Story 2 - Consultar empleados (Priority: P2)

Como usuario administrativo, quiero consultar empleados para revisar sus datos y validar su existencia.

**Why this priority**: La consulta es necesaria para el uso diario y para validar resultados de altas y actualizaciones.

**Independent Test**: Puede probarse listando empleados y obteniendo uno por clave, confirmando los datos devueltos.

**Acceptance Scenarios**:

1. **Given** que existen empleados registrados, **When** el usuario solicita el listado, **Then** el sistema devuelve todos los empleados con sus campos.
2. **Given** que existe un empleado con clave `E-001`, **When** el usuario consulta por esa clave, **Then** el sistema devuelve sus datos completos.
3. **Given** que no existe un empleado con clave `E-999`, **When** el usuario consulta por esa clave, **Then** el sistema informa que el empleado no fue encontrado.

---

### User Story 3 - Actualizar y eliminar empleado (Priority: P3)

Como usuario administrativo, quiero actualizar o eliminar un empleado para mantener la información vigente.

**Why this priority**: Completa el ciclo CRUD y permite correcciones y depuración de datos.

**Independent Test**: Puede probarse modificando campos de un empleado existente y luego eliminándolo, verificando ambos resultados.

**Acceptance Scenarios**:

1. **Given** que existe un empleado con clave `E-001`, **When** el usuario actualiza nombre, dirección o teléfono con valores válidos, **Then** el sistema persiste los cambios.
2. **Given** que existe un empleado con clave `E-001`, **When** el usuario elimina ese empleado, **Then** el sistema confirma eliminación y deja de retornarlo en consultas.
3. **Given** que no existe un empleado con clave `E-999`, **When** el usuario intenta actualizar o eliminar, **Then** el sistema informa que el empleado no fue encontrado.

### Edge Cases

- Intento de registrar empleado con `clave` vacía o nula: la operación debe rechazarse.
- Intento de registrar o actualizar `clave` sin el prefijo `E-` o con caracteres no alfanuméricos tras el prefijo: la operación debe rechazarse.
- Intento de registrar o actualizar `nombre`, `direccion` o `telefono` con más de 100 caracteres: la operación debe rechazarse.
- Intento de registrar o actualizar `nombre`, `direccion` o `telefono` vacíos: la operación debe rechazarse.
- Si no existen empleados, la consulta de listado debe responder exitosamente con colección vacía.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema MUST permitir crear empleados con los campos `clave`, `nombre`, `direccion` y `telefono`.
- **FR-002**: El sistema MUST tratar `clave` como identificador único y rechazar duplicados.
- **FR-003**: El sistema MUST permitir consultar la lista completa de empleados.
- **FR-004**: El sistema MUST permitir consultar un empleado por su `clave`.
- **FR-005**: El sistema MUST permitir actualizar `nombre`, `direccion` y `telefono` de un empleado existente.
- **FR-006**: El sistema MUST permitir eliminar un empleado existente por `clave`.
- **FR-007**: El sistema MUST validar que `nombre`, `direccion` y `telefono` tengan una longitud máxima de 100 caracteres.
- **FR-008**: El sistema MUST validar que `clave`, `nombre`, `direccion` y `telefono` sean obligatorios en creación.
- **FR-009**: El sistema MUST devolver un resultado explícito cuando no exista un empleado para la `clave` consultada, actualizada o eliminada.
- **FR-010**: El sistema MUST mantener consistencia de datos, de modo que tras una actualización o eliminación las consultas reflejen el estado más reciente.
- **FR-011**: El sistema MUST normalizar `clave` a mayúsculas antes de persistir y evaluar la unicidad sin distinguir mayúsculas/minúsculas.
- **FR-012**: El sistema MUST realizar eliminaciones físicas definitivas sin conservar registros lógicos ni bitácoras de eliminación.
- **FR-013**: El sistema MUST validar que `clave` comience con el prefijo `E-` y que el resto de la cadena sea alfanumérica mixta, respetando el límite total de 100 caracteres.

### Non-Functional Requirements

- **NFR-001**: Todas las operaciones CRUD MUST requerir autenticación previa de un único rol administrativo con acceso completo.

### Technical Constraints

- **TC-001**: La persistencia de empleados MUST realizarse en la base de datos relacional principal del proyecto mediante una tabla dedicada.

### Key Entities *(include if feature involves data)*

- **Empleado**: Representa una persona registrada en el sistema con los atributos `clave` (identificador único que inicia con `E-` seguido de cadena alfanumérica mixta en mayúsculas, máximo 100 caracteres), `nombre` (máximo 100 caracteres), `direccion` (máximo 100 caracteres) y `telefono` (máximo 100 caracteres).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: El 100% de operaciones de alta con datos válidos crean correctamente un empleado nuevo.
- **SC-002**: El 100% de intentos con `clave` duplicada son rechazados con mensaje de duplicidad.
- **SC-003**: El 100% de operaciones de actualización y eliminación sobre claves inexistentes retornan resultado de no encontrado.
- **SC-004**: El 100% de operaciones con `nombre`, `direccion` o `telefono` mayores a 100 caracteres son rechazadas por validación.
- **SC-005**: Tras una actualización o eliminación exitosa, la consulta inmediata refleja el cambio en al menos el 99% de los casos durante pruebas funcionales.

## Assumptions

- Este feature contempla un único tipo de usuario administrativo para operar el CRUD.
- La `clave` puede ser alfanumérica y se proporciona desde el cliente.
- No se incluye búsqueda avanzada ni paginación en esta iteración.
